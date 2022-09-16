package ARApi.Scaffold.Endpoints;


import ARApi.Scaffold.Database.Entities.User;
import ARApi.Scaffold.Database.Repos.UserRepository;
import ARApi.Scaffold.Endpoints.Requests.AuthRequest;
import ARApi.Scaffold.Security.ARUserDetailsService;
import ARApi.Scaffold.Shared.Enums.RegistrationOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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

    final ARUserDetailsService userDetailsService;

    @Autowired
    public AuthApi(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder, ARUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> Register(@RequestBody AuthRequest request){

        if(userRepository.findByEmail(request.email).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "email already exists");
        }

        var user = new ARApi.Scaffold.Database.Entities.User();
        user.email = request.email;
        user.password_hash = passwordEncoder.encode(request.password);
        user.registration_origin = RegistrationOrigin.PasswordRegistration;

        user = userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(userDetailsService.GetJwtForUserPricipal(user));
    }

    @GetMapping("/renew")
    public ResponseEntity<String> TryRenewJwt(){
        // requires authenticated request
        var auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // TODO: ratelimit in code / nginx conf?
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userDetailsService.GetJwtForUserPricipal(auth));
    }

    @PostMapping("/login")
    public String Login(@RequestBody AuthRequest authRequest){
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.email,
                        authRequest.password
                )
        );

        var user = (User) auth.getPrincipal();
        return userDetailsService.GetJwtForUserPricipal(user);
    }


}
