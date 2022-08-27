package ARApi.Scaffold.Endpoints;


import ARApi.Scaffold.Constants;
import ARApi.Scaffold.Database.Entities.User;
import ARApi.Scaffold.Database.Repos.UserRepository;
import ARApi.Scaffold.Endpoints.Requests.AuthRequest;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Component
@RequestMapping("auth_api")
public class AuthApi {

    final AuthenticationManager authenticationManager;

    final UserRepository userRepository;

    final PasswordEncoder passwordEncoder;

    final Environment environment;

    @Autowired
    public AuthApi(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder, Environment environment) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.environment = environment;
    }

    @PostMapping("/register")
    public ResponseEntity<HttpStatus> Register(@RequestBody AuthRequest request){

        if(userRepository.findByEmail(request.email).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "email already exists");
        }

        var user = new ARApi.Scaffold.Database.Entities.User();
        user.email = request.email;
        user.password_hash = passwordEncoder.encode(request.password);

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest){
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.email,
                        authRequest.password
                )
        );

        var user = (User) auth.getPrincipal();
        var dbUser = userRepository.findByEmail(user.getUsername()).orElseGet(ARApi.Scaffold.Database.Entities.User::new);

        var secret  = environment.getProperty(Constants.ENV_VAR_JWT_SECRET);;

        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withIssuer("auth0")
                .withSubject(dbUser.email)
                .withClaim("uuid", dbUser.uuid.toString())
                .sign(algorithm);
    }
}
