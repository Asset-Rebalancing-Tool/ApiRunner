package ARApi.Scaffold.Security;

import ARApi.Scaffold.Constants;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    public JwtAuthenticationFilter(ARUserDetailsService arUserDetailsService) {
        this.arUserDetailsService = arUserDetailsService;
        var secret = System.getenv(Constants.ENV_VAR_JWT_SECRET);

        this.verifier = JWT.require(Algorithm.HMAC256(secret))
                .withIssuer("auth0")
                .build();
    }

    ARUserDetailsService arUserDetailsService;

    JWTVerifier verifier;

    @Override
    protected void doFilterInternal(HttpServletRequest request,  HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // look for Bearer auth header
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        final String token = header.substring(7);
        DecodedJWT decodedJwt;
        try{
            decodedJwt = verifier.verify(token);
        }catch (JWTVerificationException e){
            // couldnt verify jwt, dont set access context
            e.printStackTrace();
            chain.doFilter(request, response);
            return;
        }

        // set user details on spring security context
        final UserDetails userDetails = arUserDetailsService.loadUserByUsername(decodedJwt.getSubject());
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // continue with authenticated user
        chain.doFilter(request, response);
    }

}
