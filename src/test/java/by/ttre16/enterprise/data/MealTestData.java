package by.ttre16.enterprise.data;

import by.ttre16.enterprise.controller.util.matcher.TestMatcher;
import by.ttre16.enterprise.model.Meal;
import by.ttre16.enterprise.service.util.AssertUtil;

import java.time.LocalDateTime;
import java.util.*;

import static by.ttre16.enterprise.data.UserTestData.USERS;
import static java.time.LocalDateTime.of;

public class MealTestData extends TestData {
    public static final Map<Integer, Map<Integer, Meal>> MEALS = new HashMap<>();
    public static final Integer MEAL3_ID = 3;
    public static final Integer MEAL4_ID = 4;
    public static final Integer MEAL5_ID = 5;
    public static final Integer MEAL6_ID = 6;
    public static final Integer MEAL7_ID = 7;
    public static final LocalDateTime MEAL3_DATE_TIME = of(2020, 5, 13, 20, 0);
    public static final LocalDateTime MEAL4_DATE_TIME = of(2020, 6, 1, 10, 0);
    public static final TestMatcher<Meal> MEAL_TEST_MATCHER =
            new TestMatcher<>(Meal.class, "dateTime", "user");

    static {
        Map<Integer, Meal> userMeals = new HashMap<>();

        userMeals.put(MEAL3_ID, new Meal(MEAL3_ID, 999,
                MEAL3_DATE_TIME, "some meal", USERS.get(USER_ID)));
        userMeals.put(MEAL4_ID, new Meal(MEAL4_ID, 370,
                MEAL4_DATE_TIME, "morning meal", USERS.get(USER_ID)));

        Map<Integer, Meal> adminMeals = new HashMap<>();

        adminMeals.put(MEAL5_ID, new Meal(MEAL5_ID, 550,
                LocalDateTime.now(), "first meal", USERS.get(ADMIN_ID)));
        adminMeals.put(MEAL6_ID, new Meal(MEAL6_ID, 450,
                LocalDateTime.now(), "evening meal", USERS.get(ADMIN_ID)));
        adminMeals.put(MEAL7_ID, new Meal(MEAL7_ID, 680,
                LocalDateTime.now(), "night meal", USERS.get(ADMIN_ID)));

        MEALS.put(USER_ID, userMeals);
        MEALS.put(ADMIN_ID, adminMeals);
    }

    public static Meal getNew() {
        return new Meal(null, 788,
                LocalDateTime.now(), "new meal");
    }

    public static void assertMatch(Meal expected, Meal actual) {
        AssertUtil.assertMatch(expected, actual, "dateTime", "user");
    }

    public static void assertMatch(Iterable<Meal> expected,
            Iterable<Meal> actual) {
        AssertUtil.assertMatch(expected, actual, "dateTime", "user");
    }

    public static Meal getUpdated(Integer userId, Integer mealId) {
        Meal updatedMeal = new Meal(MEALS.get(userId).get(mealId));
        updatedMeal.setDescription("updated meal");
        updatedMeal.setCalories(2300);
        return updatedMeal;
    }
}
