package by.ttre16.enterprise.util;

import by.ttre16.enterprise.dto.MealTo;
import by.ttre16.enterprise.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static by.ttre16.enterprise.util.DtoUtil.convertToDto;
import static by.ttre16.enterprise.util.DateTimeUtil.isBetweenHalfOpen;

public class MealUtil {
    public static Integer DEFAULT_CALORIES_PER_DAY = 1000;

    public static List<MealTo> getMealsWithExcess(List<Meal> meals,
            LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(Collectors.groupingBy(
                        Meal::getDate, Collectors.summingInt(
                                Meal::getCalories)));
        return meals.stream()
                .filter(meal -> isBetweenHalfOpen(
                        meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> convertToDto(
                        meal, caloriesSumByDate.get
                                (meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static ArrayList<Meal> baseMeals() {
        ArrayList<Meal> meals = new ArrayList<>();
        meals.add(new Meal(null, 500,
                (LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0)),
                "Breakfast"));
        meals.add(new Meal(null, 1000,
                (LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0)),
                "Lunch"));
        meals.add(new Meal(null, 500,
                (LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0)),
                "Dinner"));
        return meals;
    }
}
