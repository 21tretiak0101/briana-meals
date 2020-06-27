package by.ttre16.enterprise.repository.impl.inmemory;

import by.ttre16.enterprise.model.User;
import by.ttre16.enterprise.repository.MealRepository;
import by.ttre16.enterprise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository extends InMemoryBaseRepository<User>
        implements UserRepository {

    private final MealRepository mealRepository;

    @Autowired
    public InMemoryUserRepository(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    @Override
    public boolean deleteById(Integer id) {
        return this.delete(id) && mealRepository.deleteAll(id);
    }

    @Override
    public Collection<User> getAll() {
        return getCollection().stream()
                .sorted(Comparator.comparing(User::getName)
                        .thenComparing(User::getEmail))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return getCollection().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }
}
