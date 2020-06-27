package by.ttre16.enterprise.service.util;

import by.ttre16.enterprise.model.Meal;

import java.time.LocalDateTime;
import java.util.*;

import static java.time.LocalDateTime.of;


public class MealTestData extends TestData {
    public static Map<Integer, Map<Integer, Meal>> MEALS = new HashMap<>();
    public static final Integer MEAL3_ID = 3;
    public static final Integer MEAL4_ID = 4;
    public static final Integer MEAL5_ID = 5;
    public static final Integer MEAL6_ID = 6;
    public static final Integer MEAL7_ID = 7;
    public static final LocalDateTime MEAL3_DATE_TIME = of(2020, 5, 13, 20, 0);
    public static final LocalDateTime MEAL4_DATE_TIME = of(2020, 6, 1, 10, 0);

    static {
        Map<Integer, Meal> userMeals = new HashMap<>();

        userMeals.put(MEAL3_ID, new Meal(MEAL3_ID, 999,
                MEAL3_DATE_TIME, "some meal"));
        userMeals.put(MEAL4_ID, new Meal(MEAL4_ID, 370,
                MEAL4_DATE_TIME, "morning meal"));

        Map<Integer, Meal> adminMeals = new HashMap<>();

        adminMeals.put(MEAL5_ID, new Meal(MEAL5_ID, 550,
                LocalDateTime.now(), "first meal"));
        adminMeals.put(MEAL6_ID, new Meal(MEAL6_ID, 450,
                LocalDateTime.now(), "evening meal"));
        adminMeals.put(MEAL7_ID, new Meal(MEAL7_ID, 680,
                LocalDateTime.now(), "night meal"));

        MEALS.put(USER_ID, userMeals);
        MEALS.put(ADMIN_ID, adminMeals);
    }

    public static Meal getNew() {
        return new Meal(null, 788,
                LocalDateTime.now(), "new meal");
    }

    public static void assertMatch(Meal expected, Meal actual) {
        AssertUtil.assertMatch(expected, actual, "dateTime");
    }

    public static void assertMatch(Iterable<Meal> expected,
            Iterable<Meal> actual) {
        AssertUtil.assertMatch(expected, actual, "dateTime");
    }

    public static Meal getUpdated(Integer userId, Integer mealId) {
        Meal updatedMeal = new Meal(MEALS.get(userId).get(mealId));
        updatedMeal.setDescription("updated meal");
        updatedMeal.setCalories(0);
        return updatedMeal;
    }
}
