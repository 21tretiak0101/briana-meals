package by.ttre16.enterprise.service;

import by.ttre16.enterprise.annotation.QualifierRepository;
import by.ttre16.enterprise.model.User;
import by.ttre16.enterprise.repository.UserRepository;
import by.ttre16.enterprise.repository.impl.datajpa.DataJpaUserRepository;
import by.ttre16.enterprise.util.exception.NotFoundException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static by.ttre16.enterprise.util.ValidationUtil.checkNotFound;
import static by.ttre16.enterprise.util.ValidationUtil.checkNotFoundWithId;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class UserService {
    private final UserRepository repository;
    private static final Logger log = getLogger(UserService.class);

    @Autowired
    public UserService(
            @QualifierRepository(DataJpaUserRepository.class)
                    UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public User create(User user) {
        return repository.save(user);
    }

    @Transactional
    public void delete(Integer id) {
        log.warn("Delete user with id: '{}' get.", id);
        checkNotFoundWithId(this.repository.deleteById(id), id);
    }

    public User get(Integer id) {
        log.info("Get user with id: '{}'.", id);
        return repository.get(id)
                .orElseThrow(() -> new NotFoundException("User not Found"));
    }

    public User getByEmail(String email) {
        return checkNotFound(repository
                .getByEmail(email)
                .orElse(null), "email=" + email);
    }

    public List<User> getAll() {
        return new ArrayList<>(repository.getAll());
    }

    @Transactional
    public void update(User user) {
        checkNotFoundWithId(repository.save(user), user.getId());
    }
}
