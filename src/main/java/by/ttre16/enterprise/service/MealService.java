package by.ttre16.enterprise.service;

import by.ttre16.enterprise.model.Meal;
import by.ttre16.enterprise.repository.MealRepository;
import by.ttre16.enterprise.repository.impl.inmemory.InMemoryMealRepository;
import by.ttre16.enterprise.util.exception.NotFoundException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static by.ttre16.enterprise.util.DateTimeUtil.atStartOfDayOrMin;
import static by.ttre16.enterprise.util.DateTimeUtil.atEndOfDayOrMax;
import static by.ttre16.enterprise.util.ValidationUtil.checkNotFoundWithId;
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
        log.warn("Save meal with id: '{}'.", meal.getId());
        return savedMeal;
    }

    public void delete(Integer userId, Integer mealId) {
        checkNotFoundWithId(this.repository.deleteOne(userId, mealId), mealId);
        log.info("Delete meal with id: '{}'.", mealId);
    }

    public Meal getOne(Integer userId, Integer mealId) {
        log.info("Get meal with id: '{}'.", mealId);
        return repository.getOne(userId, mealId)
                .orElseThrow(() -> new NotFoundException("Meal not found"));
    }

    public List<Meal> getBetweenInclusive(@Nullable LocalDateTime startDate,
            @Nullable LocalDateTime endDate, int userId) {
        return new ArrayList<>(repository.getBetweenHalfOpen(
                atStartOfDayOrMin(startDate),
                atEndOfDayOrMax(endDate), userId));
    }

    public void update(Meal meal, int userId) {
        log.info("Update meal with id: '{}'.", meal.getId());
        checkNotFoundWithId(repository.save(userId, meal), meal.getId());
    }
}
