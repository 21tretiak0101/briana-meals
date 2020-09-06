package by.ttre16.enterprise.validation;

import by.ttre16.enterprise.dto.to.UserTo;
import by.ttre16.enterprise.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UniqueEmailValidator implements Validator {
    private final UserRepository userRepository;

    public UniqueEmailValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserTo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserTo user = (UserTo) target;
        if (userRepository.getByEmail(user.getEmail()).isPresent()) {
            errors.rejectValue("email", "duplicate email");
        }
    }
}
