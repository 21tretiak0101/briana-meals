package by.ttre16.enterprise.configuration.root.security;

import by.ttre16.enterprise.security.jwt.filter.JwtAuthorizationFilter;
import by.ttre16.enterprise.security.jwt.filter.JwtAuthenticationFilter;
import by.ttre16.enterprise.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.method.configuration
        .EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration
        .EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration
        .WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders
        .AuthenticationManagerBuilder;

import static by.ttre16.enterprise.util.web.UrlUtil.*;

@Configuration
@EnableWebSecurity
@PropertySource({"classpath:security/jwt.properties"})
@ComponentScan({"by.ttre16.enterprise.security"})
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Value("${jwt.passwordEncoderStrength}")
    private Integer passwordEncoderStrength;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public SecurityConfiguration(UserDetailsService userDetailsService,
            JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, ADMIN_REST_URL).permitAll()
                .anyRequest().authenticated()
            .and()
                .addFilter(new JwtAuthenticationFilter(
                                AUTH_REST_URL,
                                authenticationManager(),
                                jwtService)
                )
                .addFilterAfter(new JwtAuthorizationFilter(jwtService),
                        JwtAuthenticationFilter.class
                );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(passwordEncoderStrength);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
}
