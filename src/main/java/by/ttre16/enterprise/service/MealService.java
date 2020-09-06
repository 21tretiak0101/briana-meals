package by.ttre16.enterprise.service;

import by.ttre16.enterprise.dto.mapper.MealEntityMapper;
import by.ttre16.enterprise.dto.to.MealTo;
import by.ttre16.enterprise.model.Meal;
import by.ttre16.enterprise.repository.MealRepository;
import by.ttre16.enterprise.exception.NotFoundException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static by.ttre16.enterprise.util.DateTimeUtil.*;
import static by.ttre16.enterprise.util.ValidationUtil.checkNotFound;
import static by.ttre16.enterprise.util.ValidationUtil.checkNotFoundWithId;
import static java.lang.String.format;
import static java.util.Objects.isNull;
import static org.slf4j.LoggerFactory.getLogger;

@Service
@Transactional
public class MealService {
    private final MealRepository repository;
    private final MealEntityMapper mapper;
    private static final Logger log = getLogger(MealService.class);

    @Autowired
    public MealService(MealRepository repository, MealEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<Meal> getAllByUserId(Integer userId) {
        return new ArrayList<>(repository.getAll(userId));
    }

    public Meal save(Integer userId, Meal meal) {
        Meal savedMeal = this.repository.save(userId, meal);
        log.info("Save meal with id: '{}'.", meal.getId());
        return savedMeal;
    }

    public void delete(Integer userId, Integer mealId) {
        checkNotFoundWithId(this.repository.deleteOne(userId, mealId), mealId);
        log.info("Delete meal with id: '{}'.", mealId);
    }

    @Transactional(readOnly = true)
    public Meal getOne(Integer userId, Integer mealId) {
        log.info("Get meal with id: '{}'.", mealId);
        return repository.getOne(userId, mealId)
                .orElseThrow(() -> new NotFoundException("Meal not found"));
    }

    @Transactional(readOnly = true)
    public List<Meal> getBetweenInclusive(@Nullable LocalDateTime startDate,
            @Nullable LocalDateTime endDate, Integer userId) {
        return new ArrayList<>(repository.getBetweenHalfOpen(
                atStartOfDayOrMin(startDate),
                atEndOfDayOrMax(endDate), userId));
    }

    public void update(Meal meal, Integer userId) {
        log.info("Update meal with id: '{}'.", meal.getId());
        checkNotFoundWithId(repository.save(userId, meal), meal.getId());
    }

    @Transactional(readOnly = true)
    public Meal getWithUser(Integer userId, Integer id) {
        return checkNotFound(repository.getWithUser(userId, id)
                .orElse(null), format("userId=%s, id=%s", userId, id));
    }

    public List<MealTo> getMealsWithExcess(List<Meal> meals,
            LocalTime startTime, LocalTime endTime, Integer caloriesPerDay) {
        LocalTime start = isNull(startTime) ? LocalTime.MIN : startTime;
        LocalTime end = isNull(endTime) ? LocalTime.MAX : endTime;
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(Collectors.groupingBy(Meal::getDate,
                        Collectors.summingInt(Meal::getCalories)));
        return meals.stream()
                .filter(meal -> isBetweenHalfOpen(
                        meal.getDateTime().toLocalTime(), start, end))
                .map(meal -> {
                    MealTo mealTo = mapper.toDto(meal);
                    mealTo.setExcess(
                        caloriesSumByDate.get(meal.getDate()) > caloriesPerDay
                    );
                    return mealTo;
                })
                .collect(Collectors.toList());
    }
}
