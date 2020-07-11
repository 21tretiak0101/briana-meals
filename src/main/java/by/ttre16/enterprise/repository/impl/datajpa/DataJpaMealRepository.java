package by.ttre16.enterprise.repository.impl.datajpa;

import by.ttre16.enterprise.annotation.QualifierRepository;
import by.ttre16.enterprise.model.Meal;
import by.ttre16.enterprise.model.User;
import by.ttre16.enterprise.repository.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Repository
@QualifierRepository(DataJpaMealRepository.class)
public class  DataJpaMealRepository implements MealRepository {
    private final CrudMealRepository crudMealRepository;
    private final CrudUserRepository crudUserRepository;

    @Autowired
    public DataJpaMealRepository(CrudMealRepository crudMealRepository,
            CrudUserRepository crudUserRepository) {
        this.crudMealRepository = crudMealRepository;
        this.crudUserRepository = crudUserRepository;
    }

    @Override
    public Collection<Meal> getAll(Integer userId) {
        return crudMealRepository.findAll((root, query, cb) -> cb.equal(
                root.get("user").get("id"), userId));
    }

    @Override
    public Meal save(Integer userId, Meal meal) {
        User user = crudUserRepository.getOne(userId);
        meal.setUser(user);
        return crudMealRepository.save(meal);
    }

    @Override
    public boolean deleteOne(Integer userId, Integer mealId) {
        return crudMealRepository.delete(userId, mealId) == 1;
    }

    @Override
    public Optional<Meal> getOne(Integer userId, Integer mealId) {
        return crudMealRepository.findOne(((root, query, cb) -> cb.and(
                cb.equal(root.get("user").get("id"), userId),
                cb.equal(root.get("id"), mealId))));
    }

    @Override
    public boolean deleteAll(Integer userId) {
        return crudMealRepository.deleteAll(userId) != 0;
    }

    @Override
    public Collection<Meal> getBetweenHalfOpen(LocalDateTime startDateTime,
            LocalDateTime endDateTime, Integer userId) {
        return crudMealRepository.findAll((root, query, cb) -> cb.and(
                cb.equal(root.get("user").get("id"), userId),
                cb.greaterThanOrEqualTo(root.get("dateTime"), startDateTime),
                cb.lessThan(root.get("dateTime"), endDateTime)),
                Sort.by("dateTime").descending());
    }
}
