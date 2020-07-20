package by.ttre16.enterprise.repository.impl.inmemory;

import by.ttre16.enterprise.annotation.QualifierRepository;
import by.ttre16.enterprise.model.User;
import by.ttre16.enterprise.repository.MealRepository;
import by.ttre16.enterprise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

import static by.ttre16.enterprise.util.ProfileUtil.IN_MEMORY;

@Repository
@Profile(IN_MEMORY)
public class InMemoryUserRepository extends InMemoryBaseRepository<User>
        implements UserRepository {

    private final MealRepository mealRepository;

    @Autowired
    public InMemoryUserRepository(
            @QualifierRepository(InMemoryMealRepository.class)
                    MealRepository mealRepository) {
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
