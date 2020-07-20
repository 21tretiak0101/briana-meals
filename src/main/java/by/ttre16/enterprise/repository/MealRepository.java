package by.ttre16.enterprise.repository;

import by.ttre16.enterprise.model.Meal;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface MealRepository extends AppRepository {
     Collection<Meal> getAll(Integer userId);

     Meal save(Integer id, Meal meal);

     boolean deleteOne(Integer userId, Integer mealId);

     Optional<Meal> getOne(Integer userId, Integer mealId);

     boolean deleteAll(Integer userId);

     default Optional<Meal> getWithUser(Integer userId, Integer id) {
          throw new UnsupportedOperationException();
     }

     Collection<Meal> getBetweenHalfOpen(LocalDateTime startDateTime,
               LocalDateTime endDateTime, Integer userId);
}
