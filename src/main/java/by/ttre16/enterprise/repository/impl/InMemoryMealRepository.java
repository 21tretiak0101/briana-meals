package by.ttre16.enterprise.repository.impl;

import by.ttre16.enterprise.model.Meal;
import by.ttre16.enterprise.repository.MealRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static by.ttre16.enterprise.util.MealUtil.baseMeals;

public class InMemoryMealRepository implements MealRepository {

    private final AtomicInteger counter = new AtomicInteger(0);

    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();

    {
        baseMeals().forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meals.put(meal.getId(), meal);
            return meal;
        }
        return meals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean deleteById(Integer id) {
        return meals.remove(id) != null;
    }

    @Override
    public Optional<Meal> getById(Integer id) {
        return Optional.ofNullable(meals.get(id));
    }

    @Override
    public Collection<Meal> getAll() {
        return meals.values();
    }

    @Override
    public boolean existsById(Integer id) {
        return meals.containsKey(id);
    }
}
