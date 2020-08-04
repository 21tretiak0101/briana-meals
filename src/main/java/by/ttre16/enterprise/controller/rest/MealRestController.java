package by.ttre16.enterprise.controller.rest;

import by.ttre16.enterprise.dto.MealTo;
import by.ttre16.enterprise.model.Meal;
import by.ttre16.enterprise.service.MealService;
import by.ttre16.enterprise.security.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static by.ttre16.enterprise.util.DateTimeUtil.atEndOfDayOrMax;
import static by.ttre16.enterprise.util.DateTimeUtil.atStartOfDayOrMin;
import static by.ttre16.enterprise.util.MealUtil.getMealsWithExcess;
import static by.ttre16.enterprise.util.ValidationUtil.assureIdConsistent;
import static by.ttre16.enterprise.util.ValidationUtil.checkNew;

@RestController
@RequestMapping("/rest/meals")
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final MealService service;
    private final SecurityUtil securityUtil;

    @Autowired
    public MealRestController(MealService service, SecurityUtil securityUtil) {
        this.service = service;
        this.securityUtil = securityUtil;
    }

    public List<Meal> getAll() {
        Integer userId = getUserId();
        log.info("getAll");
        return service.getAllByUserId(userId);
    }

    public Meal get(int id) {
        Integer userId = getUserId();
        log.info("get {}", id);
        return service.getOne(userId, id);
    }

    public void create(Meal meal) {
        Integer userId = getUserId();
        log.info("create {}", meal);
        checkNew(meal);
        service.save(userId, meal);
    }

    public void delete(Integer id) {
        Integer userId = getUserId();
        log.info("delete {}", id);
        service.delete(userId, id);
    }

    public void update(Meal meal, Integer id) {
        Integer userId = getUserId();
        assureIdConsistent(meal, id);
        log.info("update {} with id={}", meal, id);
        service.update(meal, userId);
    }

    public List<MealTo> getBetween(@Nullable LocalDate startDate,
            @Nullable LocalDate endDate,@Nullable LocalTime startTime,
            @Nullable LocalTime endTime) {
        Integer userId = getUserId();
        log.info("getBetween dates({} - {}) time({} - {}) for user {}",
                startDate, endDate, startTime, endTime, userId);
        List<Meal> mealsDateFiltered = service.getBetweenInclusive(
                atStartOfDayOrMin(startDate, startTime),
                atEndOfDayOrMax(endDate, endTime),
                userId);
        return getMealsWithExcess(mealsDateFiltered,
                startTime, endTime, securityUtil.getAuthUserCaloriesPerDay());
    }

    private Integer getUserId() {
        return securityUtil.getAuthUserId();
    }
}
