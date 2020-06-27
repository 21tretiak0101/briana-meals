package by.ttre16.enterprise.repository.impl.inmemory;

import by.ttre16.enterprise.model.Meal;
import by.ttre16.enterprise.repository.MealRepository;
import by.ttre16.enterprise.util.DateTimeUtil;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, InMemoryBaseRepository<Meal>> userMeals =
            new ConcurrentHashMap<>();

    @Override
    public Meal save(Integer userId, Meal meal) {
        InMemoryBaseRepository<Meal> meals = userMeals
                .computeIfAbsent(userId, uid -> new InMemoryBaseRepository<>());
        return meals.save(meal);
    }

    @Override
    public boolean deleteOne(Integer userId, Integer mealId) {
        InMemoryBaseRepository<Meal> meals = userMeals.get(userId);
        return nonNull(meals) && meals.delete(mealId);
    }

    @Override
    public Optional<Meal> getOne(Integer userId, Integer mealId) {
        InMemoryBaseRepository<Meal> meals = userMeals.get(userId);
        return  isNull(meals)
                ? Optional.empty()
                : meals.get(mealId);
    }

    @Override
    public Collection<Meal> getAll(Integer id) {
        return filterByPredicate(id, meal -> true);
    }

    @Override
    public boolean deleteAll(Integer userId) {
        return nonNull(userMeals.remove(userId));
    }

    @Override
    public Collection<Meal> getBetweenHalfOpen(LocalDateTime startDateTime,
            LocalDateTime endDateTime, int userId) {
        return filterByPredicate(userId,
                meal -> DateTimeUtil.isBetweenHalfOpen(
                        meal.getDateTime(), startDateTime, endDateTime));
    }

    public Collection<Meal> filterByPredicate(Integer userId,
            Predicate<? super Meal> filter) {
        InMemoryBaseRepository<Meal> meals = userMeals.get(userId);
        return isNull(meals)
                ? Collections.emptyList()
                : meals.getCollection().stream()
                    .filter(filter)
                    .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                    .collect(Collectors.toList());
    }
}
