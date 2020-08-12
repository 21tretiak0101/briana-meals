package by.ttre16.enterprise.controller;

import by.ttre16.enterprise.dto.MealTo;
import by.ttre16.enterprise.model.Meal;
import by.ttre16.enterprise.service.MealService;
import by.ttre16.enterprise.util.entity.DtoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static by.ttre16.enterprise.util.DateTimeUtil.atEndOfDayOrMax;
import static by.ttre16.enterprise.util.DateTimeUtil.atStartOfDayOrMin;
import static by.ttre16.enterprise.util.entity.MealUtil.getMealsWithExcess;
import static by.ttre16.enterprise.util.ValidationUtil.assureIdConsistent;
import static by.ttre16.enterprise.util.ValidationUtil.checkNew;

public abstract class AbstractMealController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService mealService;

    public List<Meal> getAll(Integer userId) {
        log.info("Get all");
        return mealService.getAllByUserId(userId);
    }

    public Meal get(Integer userId, Integer mealId) {
        log.info("Get {}", mealId);
        return mealService.getOne(userId, mealId);
    }

    public Meal create(Meal meal, Integer userId) {
        checkNew(meal);
        log.info("Create {}", meal);
        return mealService.save(userId, meal);
    }

    public void delete(Integer userId, Integer mealId) {
        log.info("Delete {}", mealId);
        mealService.delete(userId, mealId);
    }

    public void update(Integer userId, Meal meal, Integer id) {
        assureIdConsistent(meal, id);
        log.info("Update {} with id={}", meal, id);
        mealService.update(meal, userId);
    }

    public List<MealTo> getBetween(@Nullable LocalDate startDate,
        @Nullable LocalDate endDate, @Nullable LocalTime startTime,
        @Nullable LocalTime endTime, Integer userId, Integer caloriesPerDay) {
        log.info("Get between dates({} - {}) time({} - {}) for user: {}",
                startDate, endDate, startTime, endTime, userId);
        List<Meal> meals =  mealService.getBetweenInclusive(
                atStartOfDayOrMin(startDate, startTime),
                atEndOfDayOrMax(endDate, endTime),
                userId);
        return getMealsWithExcess(meals, startTime, endTime, caloriesPerDay);
    }
}
