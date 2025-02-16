package ds.microservice.user.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import ds.microservice.user.dtos.LoginRequestDTO;
import ds.microservice.user.repositories.PersonRepository;
import ds.microservice.user.security.service.UserDetailsServiceBean;
import ds.microservice.user.security.util.JwtUtil;
import ds.microservice.user.security.util.SecurityConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;

    private final UserDetailsServiceBean userDetailsServiceBean;

    public LoginFilter(ObjectMapper objectMapper, AuthenticationManager authenticationManager, UserDetailsServiceBean userDetailsServiceBean) {
        this.objectMapper = objectMapper;
        this.userDetailsServiceBean = userDetailsServiceBean;
        this.setAuthenticationManager(authenticationManager);
        this.setFilterProcessesUrl(SecurityConstants.LOGIN_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            LoginRequestDTO authenticationRequest = objectMapper.readValue(
                    request.getInputStream(),
                    LoginRequestDTO.class
            );
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(),
                    authenticationRequest.getPassword()
            );

            return super.getAuthenticationManager().authenticate(authentication);
        } catch (Exception e) {
            log.error(e.getMessage());

            throw new BadCredentialsException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // Generate the JWT token
        String accessToken = JwtUtil.generateJwtToken(
                ((User) authResult.getPrincipal()).getUsername(),
                authResult.getAuthorities()
        );

        // Add the token as a cookie (optional)
        response.addCookie(JwtUtil.buildCookie(SecurityConstants.JWT_TOKEN, accessToken));

        // Send the token in the response body
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("jwtToken", accessToken);
        PersonRepository personRepository = userDetailsServiceBean.getPersonRepository();
        responseBody.put("id", personRepository.findByEmail(((User) authResult.getPrincipal()).getUsername()).get().getPersonId().toString());

        // Set the content type and send the JSON response
        response.setContentType("application/json");
        response.setStatus(HttpStatus.OK.value());

        // Convert the response body to JSON and write to the response
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), responseBody);
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        objectMapper.writeValue(response.getWriter(), new Exception(failed.getMessage()));
    }
}