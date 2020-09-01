package by.ttre16.enterprise.controller.rest;

import by.ttre16.enterprise.controller.AbstractControllerTest;
import by.ttre16.enterprise.model.User;
import by.ttre16.enterprise.security.jwt.JwtService;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static by.ttre16.enterprise.data.UserTestData.USERS;

public abstract class AbstractRestControllerTest
        extends AbstractControllerTest {
    @Autowired
    protected JwtService jwtService;

    protected static String authorizationHeader;

    protected static String bearerToken;

    @Before
    public void init() {
        authorizationHeader = jwtService.getAuthorizationHeader();
        bearerToken = generateBearerToken(USERS.get(getTestUserId()));
    }

    public abstract Integer getTestUserId();

    protected String generateBearerToken(User user) {
        Set<SimpleGrantedAuthority> userAuthorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(
                        "ROLE_" + role.getName()
                ))
                .collect(Collectors.toSet());

        return "Bearer " + jwtService
                .createToken(
                        user.getId(),
                        user.getEmail(),
                        userAuthorities
                );
    }
}
