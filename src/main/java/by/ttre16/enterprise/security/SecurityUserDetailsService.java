package by.ttre16.enterprise.security;

import by.ttre16.enterprise.model.User;
import by.ttre16.enterprise.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
public class SecurityUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public SecurityUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        User user = userRepository.getByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        (format("User with email '%s' not found.", email))));
        return new SecurityUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                user.getRoles()
        );
    }
}
