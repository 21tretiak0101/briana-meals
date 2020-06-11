package by.ttre16.enterprise.service;

import by.ttre16.enterprise.model.Meal;
import by.ttre16.enterprise.repository.MealRepository;
import by.ttre16.enterprise.repository.impl.InMemoryMealRepository;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;
import static org.slf4j.LoggerFactory.getLogger;

public class MealService {
    private final MealRepository repository;
    private static final Logger log = getLogger(MealService.class);

    public MealService() {
        this.repository = new InMemoryMealRepository();
    }

    public List<Meal> getAll() {
        return new ArrayList<>(repository.getAll());
    }

    public void save(Meal meal) {
        Meal savedMeal = this.repository.save(meal);
        if (nonNull(savedMeal)) {
            log.info(meal.isNew()
                    ? "Meal with id: '{}' created."
                    : "Meal with id: '{}' updated.",
                    savedMeal.getId());
        } else {
            log.warn("Meal with id: '{}' doesn't exist.", meal.getId());
        }
    }

    public void delete(Integer id) {
        boolean isDeleted = this.repository.deleteById(id);
        if (isDeleted) {
            log.info("Meal with id: '{}' removed.", id);
        } else {
            log.warn("Meal with id: '{}' doesn't exist.", id);
        }
    }
}
