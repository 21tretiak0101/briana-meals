package by.ttre16.enterprise.service;

import by.ttre16.enterprise.model.Meal;
import by.ttre16.enterprise.model.User;
import by.ttre16.enterprise.repository.MealRepository;
import by.ttre16.enterprise.repository.impl.InMemoryMealRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static by.ttre16.enterprise.util.DateTimeUtil.atStartOfDayOrMin;
import static by.ttre16.enterprise.util.DateTimeUtil.atStartOfNextDayOrMax;
import static by.ttre16.enterprise.util.ValidationUtil.checkNotFoundWithId;
import static java.util.Objects.nonNull;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class MealService {
    private final MealRepository repository;
    private static final Logger log = getLogger(MealService.class);

    @Autowired
    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public MealService() {
        this.repository = new InMemoryMealRepository();
    }

    public List<Meal> getAllByUserId(Integer userId) {
        return new ArrayList<>(repository.getAll(userId));
    }

    public Meal save(Integer userId, Meal meal) {
        Meal savedMeal = this.repository.save(userId, meal);
        if (nonNull(savedMeal)) {
            log.info(meal.isNew()
                    ? "Meal with id: '{}' created."
                    : "Meal with id: '{}' updated.",
                    savedMeal.getId());
        } else {
            log.warn("Meal with id: '{}' doesn't exist.", meal.getId());
        }
        return savedMeal;
    }

    public void delete(Integer userId, Integer mealId) {
        boolean isDeleted = this.repository.deleteOne(userId, mealId);
        if (isDeleted) {
            log.info("Meal with id: '{}' removed.", mealId);
        } else {
            log.warn("Meal with id: '{}' doesn't exist.", mealId);
        }
    }

    public Meal getOne(Integer userId, Integer mealId) {
        return repository.getOne(userId, mealId).orElse(null);
    }

    public List<Meal> getBetweenInclusive(@Nullable LocalDate startDate,
            @Nullable LocalDate endDate, int userId) {
        return new ArrayList<>(repository.getBetweenHalfOpen(
                atStartOfDayOrMin(startDate),
                atStartOfNextDayOrMax(endDate), userId));
    }

    public void update(Meal meal, int userId) {
        checkNotFoundWithId(repository.save(userId, meal), meal.getId());
    }
}
