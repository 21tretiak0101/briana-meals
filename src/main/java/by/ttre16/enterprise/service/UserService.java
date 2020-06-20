package by.ttre16.enterprise.service;

import by.ttre16.enterprise.model.User;
import by.ttre16.enterprise.repository.UserRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static by.ttre16.enterprise.util.ValidationUtil.checkNotFound;
import static by.ttre16.enterprise.util.ValidationUtil.checkNotFoundWithId;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class UserService {
    private final UserRepository repository;
    private static final Logger log = getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User create(User user) {
        return repository.save(user);
    }

    public void delete(Integer id) {
        boolean isDeleted = this.repository.deleteById(id);
        if (isDeleted) {
            log.info("Meal with id: '{}' removed.", id);
        } else {
            log.warn("Meal with id: '{}' doesn't exist.", id);
        }
    }

    public User get(int id) {
        return repository.get(id).orElse(null);
    }

    public User getByEmail(String email) {
        return checkNotFound(repository
                .getByEmail(email)
                .orElse(null), "email=" + email);
    }

    public Collection<User> getAll() {
        return repository.getAll();
    }

    public void update(User user) {
        checkNotFoundWithId(repository.save(user), user.getId());
    }
}
