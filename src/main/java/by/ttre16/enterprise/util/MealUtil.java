package by.ttre16.enterprise.util;

import by.ttre16.enterprise.dto.MealTo;
import by.ttre16.enterprise.model.Meal;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static by.ttre16.enterprise.util.DtoUtil.convertToDto;
import static by.ttre16.enterprise.util.DateTimeUtil.isBetweenHalfOpen;
import static java.util.Objects.isNull;

public class MealUtil {
    public static Integer DEFAULT_CALORIES_PER_DAY = 2000;

    public static List<MealTo> getMealsWithExcess(List<Meal> meals,
            LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        LocalTime start = isNull(startTime) ? LocalTime.MIN : startTime;
        LocalTime end = isNull(endTime) ? LocalTime.MAX : endTime;
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(Collectors.groupingBy(Meal::getDate,
                        Collectors.summingInt(Meal::getCalories)));
        return meals.stream()
                .filter(meal -> isBetweenHalfOpen(
                        meal.getDateTime().toLocalTime(), start, end))
                .map(meal -> convertToDto(
                        meal, caloriesSumByDate.get
                                (meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }
}
