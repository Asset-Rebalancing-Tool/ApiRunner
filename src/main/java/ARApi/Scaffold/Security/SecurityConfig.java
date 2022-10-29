package ARApi.Scaffold.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    public SecurityConfig(ARUserDetailsService userDetailsService, JwtAuthenticationFilter authenticationFilter, Environment env) {
        this.userDetailsService = userDetailsService;
        this.authenticationFilter = authenticationFilter;
        this.env = env;
    }

    private final ARUserDetailsService userDetailsService;

    private final JwtAuthenticationFilter authenticationFilter;

    private static String CLIENT_PROPERTY_KEY
            = "spring.security.oauth2.client.registration.";

    private final Environment env;



    private static final List<String> clients = List.of("google");



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Autowired
    void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests((authorize) -> authorize
                        .antMatchers("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs").permitAll()
                        .antMatchers(HttpMethod.POST, "/auth_api/login", "/auth_api/register").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

         /* TODO: Implement Frontend first
         http.oauth2Login().userInfoEndpoint().userService(userDetailsService)
                 .and().successHandler((request, response, authentication) -> {
                     var oauthUser = (DefaultOidcUser) authentication.getPrincipal();

                     var token  = userDetailsService.processOAuthPostLogin(oauthUser, response);
                     if(token != null){
                         response.setHeader("Authorization", token);
                         // success
                         // TODO: mit sebi redirect url besprechen
                         response.sendRedirect("/swagger-ui/index.html");
                     }
                     // TODO: redirect oauth fail
                     response.setHeader("Authorization", "nogger");
                     response.sendRedirect("/swagger-ui/index.html");
                     //response.sendError(409);

                 });
        */
        return http.build();
    }
}
