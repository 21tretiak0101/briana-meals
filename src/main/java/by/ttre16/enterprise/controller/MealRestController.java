package by.ttre16.enterprise.controller;

import by.ttre16.enterprise.dto.MealTo;
import by.ttre16.enterprise.model.Meal;
import by.ttre16.enterprise.service.MealService;
import by.ttre16.enterprise.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static by.ttre16.enterprise.util.DateTimeUtil.atEndOfDayOrMax;
import static by.ttre16.enterprise.util.DateTimeUtil.atStartOfDayOrMin;
import static by.ttre16.enterprise.util.MealUtil.getMealsWithExcess;
import static by.ttre16.enterprise.util.SecurityUtil.authUserCaloriesPerDay;
import static by.ttre16.enterprise.util.ValidationUtil.assureIdConsistent;
import static by.ttre16.enterprise.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<Meal> getAll() {
        int userId = SecurityUtil.authUserId();
        log.info("getAll");
        return service.getAllByUserId(userId);
    }

    public Meal get(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("get {}", id);
        return service.getOne(userId, id);
    }

    public void create(Meal meal) {
        int userId = SecurityUtil.authUserId();
        log.info("create {}", meal);
        checkNew(meal);
        service.save(userId, meal);
    }

    public void delete(Integer id) {
        int userId = SecurityUtil.authUserId();
        log.info("delete {}", id);
        service.delete(userId, id);
    }

    public void update(Meal meal, Integer id) {
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(meal, id);
        log.info("update {} with id={}", meal, id);
        service.update(meal, userId);
    }

    public List<MealTo> getBetween(@Nullable LocalDate startDate,
            @Nullable LocalDate endDate,@Nullable LocalTime startTime,
            @Nullable LocalTime endTime) {
        int userId = SecurityUtil.authUserId();
        log.info("getBetween dates({} - {}) time({} - {}) for user {}",
                startDate, endDate, startTime, endTime, userId);
        List<Meal> mealsDateFiltered = service.getBetweenInclusive(
                atStartOfDayOrMin(startDate, startTime),
                atEndOfDayOrMax(endDate, endTime),
                userId);
        return getMealsWithExcess(mealsDateFiltered,
                startTime, endTime, authUserCaloriesPerDay());
    }
}
