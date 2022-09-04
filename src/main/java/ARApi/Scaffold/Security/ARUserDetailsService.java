package ARApi.Scaffold.Security;

import ARApi.Scaffold.Constants;
import ARApi.Scaffold.Database.Entities.User;
import ARApi.Scaffold.Database.Repos.UserRepository;
import ARApi.Scaffold.Shared.Enums.RegistrationOrigin;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import liquibase.repackaged.org.apache.commons.collections4.Get;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class ARUserDetailsService extends DefaultOAuth2UserService implements UserDetailsService {

    UserRepository userRepository;

    final Environment environment;

    @Autowired
    public ARUserDetailsService(UserRepository userRepository, Environment environment) {
        this.userRepository = userRepository;
        this.environment = environment;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var all = userRepository.findAll();
        var optUser = userRepository.findByEmail(username);
        if(optUser.isPresent()){
            return optUser.get();
        }

        throw new UsernameNotFoundException(username);
    }

    /**
     *
     * @param user user from oauth flow
     * @return system specific jwt
     */
    public String processOAuthPostLogin(DefaultOidcUser user, HttpServletResponse response) throws IOException {
        var optUser = userRepository.findByEmail(user.getEmail());
        if(optUser.isPresent()){
            var dbUser = optUser.get();
            if(dbUser.registration_origin != RegistrationOrigin.OAuthRegistration){
                return null;
            }
            return GetJwtForUserPricipal(dbUser);
        }

        // user is new from flow, has to be registered in database
        var dbUserInsert = new User();
        dbUserInsert.email = user.getEmail();
        dbUserInsert.registration_origin = RegistrationOrigin.OAuthRegistration;
        return GetJwtForUserPricipal(userRepository.saveAndFlush(dbUserInsert));
    }

    public String GetJwtForUserPricipal(User user){
        var dbUser = userRepository.findByEmail(user.getUsername()).orElseGet(ARApi.Scaffold.Database.Entities.User::new);

        var secret  = environment.getProperty(Constants.ENV_VAR_JWT_SECRET);

        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withIssuer("auth0")
                .withSubject(dbUser.email)
                .withClaim("uuid", dbUser.uuid.toString())
                .withExpiresAt(Instant.now().plus(30, ChronoUnit.MINUTES))
                .sign(algorithm);
    }
}
