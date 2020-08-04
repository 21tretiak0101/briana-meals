package by.ttre16.enterprise.service;

import by.ttre16.enterprise.model.User;
import by.ttre16.enterprise.repository.UserRepository;
import by.ttre16.enterprise.util.exception.NotFoundException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static by.ttre16.enterprise.util.ValidationUtil.checkNotFound;
import static by.ttre16.enterprise.util.ValidationUtil.checkNotFoundWithId;
import static org.slf4j.LoggerFactory.getLogger;

@Service
@Transactional
public class UserService {
    private final UserRepository repository;
    private static final Logger log = getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @CacheEvict(value = "users", allEntries = true)
    public User create(User user) {
        return repository.save(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void delete(Integer id) {
        log.info("Delete user with id: '{}' get.", id);
        checkNotFoundWithId(this.repository.deleteById(id), id);
    }

    @Transactional(readOnly = true)
    public User get(Integer id) {
        log.info("Get user with id: '{}'.", id);
        return repository.get(id)
                .orElseThrow(() -> new NotFoundException("User not Found"));
    }

    @Transactional(readOnly = true)
    public User getByEmail(String email) {
        return checkNotFound(repository
                .getByEmail(email)
                .orElse(null), "email=" + email);
    }

    @Cacheable("users")
    @Transactional(readOnly = true)
    public List<User> getAll() {
        return new ArrayList<>(repository.getAll());
    }

    public void update(User user) {
        checkNotFoundWithId(repository.save(user), user.getId());
    }

    @Transactional(readOnly = true)
    public User getWithMeals(Integer id) {
        return checkNotFound(repository.getWithMeals(id).orElse(null),
                "id=" + id);
    }
}
