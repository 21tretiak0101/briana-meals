package by.ttre16.enterprise.repository;

import by.ttre16.enterprise.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository extends AppRepository {
    Optional<User> getByEmail(String email);

    Collection<User> getAll();

    User save(User user);

    Optional<User> get(Integer id);

    boolean deleteById(Integer id);
}
