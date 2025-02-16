package ds.microservice.user.security.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Properties;

@Slf4j
@UtilityClass
public class JwtUtil {

    public String secretKey;
    public Integer tokenExpirationDays;

    static {
        try (InputStream inputStream = JwtUtil.class.getClassLoader().getResourceAsStream("application.properties");) {
            Properties properties = new Properties();
            properties.load(inputStream);
            JwtUtil.secretKey = properties.getProperty("secret-key");
            JwtUtil.tokenExpirationDays = Integer.parseInt(properties.getProperty("token-expiration-days"));
        } catch (IOException | NumberFormatException e) {
            log.error(e.getMessage());
        }
    }

    public String generateJwtToken(String email, Collection<? extends GrantedAuthority> role) {
        return Jwts.builder()
                .subject(email)
                .claim("role", role.stream().map(GrantedAuthority::getAuthority).toList())
                .expiration(JwtUtil.getExpirationDate(JwtUtil.tokenExpirationDays))
                .signWith(JwtUtil.getSigningKey(JwtUtil.secretKey))
                .compact();
    }

    public Cookie buildCookie(String cookieName, String cookieValue) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setPath("/");
        cookie.setMaxAge(JwtUtil.tokenExpirationDays);

        return cookie;
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        return request.getHeader("Authorization") == null
                ? null
                : request.getHeader("Authorization").split("Bearer ")[1];
    }

    private Date getExpirationDate(Integer tokenExpirationDays) {
        return Date.valueOf(LocalDate.now().plusDays(tokenExpirationDays));
    }

    public Key getSigningKey(String secretKey) {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}