package by.ttre16.enterprise.data;

import by.ttre16.enterprise.controller.util.matcher.TestMatcher;
import by.ttre16.enterprise.dto.to.MealTo;
import by.ttre16.enterprise.service.util.AssertUtil;

public class MealToTestData {
    public static final TestMatcher<MealTo> MEAL_TO_TEST_MATCHER
            = new TestMatcher<>(MealTo.class, "dateTime");

    public static void assertMatch(MealTo expected, MealTo actual) {
        AssertUtil.assertMatch(expected, actual, "dateTime");
    }

    public static void assertMatch(Iterable<MealTo> expected,
        Iterable<MealTo> actual) {
        AssertUtil.assertMatch(expected, actual, "dateTime");
    }
}
