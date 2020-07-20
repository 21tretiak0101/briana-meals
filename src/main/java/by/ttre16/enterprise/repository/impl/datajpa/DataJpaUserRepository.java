package by.ttre16.enterprise.repository.impl.datajpa;

import by.ttre16.enterprise.model.User;
import by.ttre16.enterprise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

import static by.ttre16.enterprise.util.ProfileUtil.DATA_JPA;

@Repository
@Profile(DATA_JPA)
public class DataJpaUserRepository implements UserRepository {
    private final CrudUserRepository crudUserRepository;

    @Autowired
    public DataJpaUserRepository(CrudUserRepository crudUserRepository) {
        this.crudUserRepository = crudUserRepository;
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return crudUserRepository.findByEmail(email);
    }

    @Override
    public Collection<User> getAll() {
        return crudUserRepository.findAll();
    }

    @Override
    public User save(User user) {
        return crudUserRepository.save(user);
    }

    @Override
    public Optional<User> get(Integer id) {
        return crudUserRepository.findById(id);
    }

    @Override
    public boolean deleteById(Integer id) {
        return crudUserRepository.delete(id) == 1;
    }

    @Override
    public Optional<User> getWithMeals(Integer id) {
        return crudUserRepository.getWithMeals(id);
    }
}
