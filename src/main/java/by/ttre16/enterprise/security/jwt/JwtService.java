package by.ttre16.enterprise.security.jwt;

import by.ttre16.enterprise.model.AuthenticatedUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import java.util.*;
import java.util.stream.Collectors;

import static java.sql.Date.valueOf;
import static java.time.LocalDate.now;
import static org.springframework.util.StringUtils.isEmpty;

@Service
public class JwtService {
    @Value("${jwt.private-key}")
    private String key;

    @Value("${jwt.authorizationHeader}")
    private String authorizationHeader;

    @Value("${jwt.validity}")
    private Integer validity;

    private String token;

    @PostConstruct
    public void init() {
        key = Base64.getEncoder().encodeToString(key.getBytes());
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken(HttpServletRequest request) {
        return request.getHeader(authorizationHeader);
    }

    public boolean isValidToken() {
        return !isEmpty(token)
                && token.startsWith("Bearer ")
                && resolveToken().getExpiration().after(new Date());
    }

    private Set<SimpleGrantedAuthority> getAuthorities() {
        @SuppressWarnings("unchecked")
        List<Map<String, String>> parsedAuthorities =
                (List<Map<String, String>>) resolveToken().get("authorities");
        return parsedAuthorities.stream()
                .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                .collect(Collectors.toSet());
    }

    private Claims resolveToken() {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(key.getBytes()))
                .build()
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody();
    }

    public String createToken(Integer id, String email,
            Collection<? extends GrantedAuthority> roles) {
        return Jwts.builder()
                .setSubject(email)
                .claim("userId", id)
                .claim("authorities", roles)
                .setIssuedAt(new Date())
                .setExpiration(valueOf(now().plusDays(validity)))
                .signWith(Keys.hmacShaKeyFor(key.getBytes()))
                .compact();
    }

    public String getAuthorizationHeader() {
        return authorizationHeader;
    }

    public Authentication getAuthentication() {
        return new UsernamePasswordAuthenticationToken(
                new AuthenticatedUser(
                        parseId(),
                        parseEmail()
                ),
                null,
                getAuthorities()
        );
    }

    private String parseEmail() {
        return resolveToken().getSubject();
    }

    private Integer parseId() {
        return resolveToken().get("userId", Integer.class);
    }
}
