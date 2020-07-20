package by.ttre16.enterprise.service;

import by.ttre16.enterprise.model.Meal;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.util.List;

import static by.ttre16.enterprise.service.util.AssertUtil.assertThrowsNotFoundException;
import static by.ttre16.enterprise.service.util.MealTestData.*;
import static java.time.LocalDateTime.now;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

public abstract class AbstractMealServiceTest extends AbstractServiceTest {
    @Autowired
    private MealService service;

    @Test
    public void getAllByUserId() {
        List<Meal> userMeals = service.getAllByUserId(USER_ID);
        List<Meal> adminMeals = service.getAllByUserId(ADMIN_ID);
        assertMatch(MEALS.get(USER_ID).values(), userMeals);
        assertMatch(MEALS.get(ADMIN_ID).values(), adminMeals);
    }

    @Test
    public void getAllWhereUserNotFound() {
        List<Meal> meals = service.getAllByUserId(NOT_FOUND_ID);
        assertTrue(meals.isEmpty());
    }

    @Test
    public void save() {
        Meal savedMeal = service.save(USER_ID, getNew());
        Meal newMeal = getNew();
        Integer mid = savedMeal.getId();
        newMeal.setId(mid);
        assertMatch(newMeal, savedMeal);
        assertMatch(newMeal, service.getOne(USER_ID, mid));
    }

    @Test
    public void saveWithSameTime() {
        Meal newMeal = getNew();
        newMeal.setDateTime(MEAL3_DATE_TIME);
        assertThrows(DataAccessException.class,
                () -> service.save(USER_ID, newMeal));
    }

    @Test
    public void delete() {
        service.delete(USER_ID, MEAL3_ID);
        assertThrowsNotFoundException(() -> service.getOne(USER_ID, MEAL3_ID));
    }

    @Test
    public void deleteWhereUserNotFound() {
        assertThrowsNotFoundException(
                () -> service.delete(NOT_FOUND_ID, MEAL3_ID));
    }

    @Test
    public void deleteWhereMealNotFound() {
        assertThrowsNotFoundException(
                () -> service.delete(ADMIN_ID, NOT_FOUND_ID));
    }

    @Test
    public void getOne() {
        Meal meal = MEALS.get(USER_ID).get(MEAL3_ID);
        assertMatch(meal, service.getOne(USER_ID, MEAL3_ID));
    }

    @Test
    public void getOneWhereMealNotFound() {
        assertThrowsNotFoundException(
                () -> service.getOne(USER_ID, NOT_FOUND_ID));
    }

    @Test
    public void getOneWhereUserNotFound() {
        assertThrowsNotFoundException(
                () -> service.getOne(NOT_FOUND_ID, MEAL3_ID));
    }

    @Test
    public void getBetweenInclusive1() {
        List<Meal> userMeals = service
                .getBetweenInclusive(MEAL3_DATE_TIME, now(), USER_ID);
        assertMatch(service.getAllByUserId(USER_ID), userMeals);
    }

    @Test
    public void getBetweenInclusive2() {
        List<Meal> userMeals = service.getBetweenInclusive(MEAL3_DATE_TIME,
                MEAL4_DATE_TIME, USER_ID);
        Meal expected = MEALS.get(USER_ID).get(MEAL3_ID);
        assertMatch(singletonList(expected), userMeals);
    }

    @Test
    public void getBetweenInclusiveWithNullDate() {
        List<Meal> meals = service.getBetweenInclusive(
                null, null, USER_ID);
        assertMatch(service.getAllByUserId(USER_ID), meals);
    }

    @Test
    public void update() {
        Meal updatedMeal = getUpdated(ADMIN_ID, MEAL6_ID);
        service.update(updatedMeal, ADMIN_ID);
        assertMatch(getUpdated(ADMIN_ID, MEAL6_ID),
                service.getOne(ADMIN_ID, MEAL6_ID));
    }

    @Test
    public void getWithUser() {
         Meal userMeal = MEALS.get(USER_ID).get(MEAL3_ID);
         Meal adminMeal = MEALS.get(ADMIN_ID).get(MEAL5_ID);
         assertMatch(userMeal, service.getWithUser(USER_ID, MEAL3_ID));
         assertMatch(adminMeal, service.getWithUser(ADMIN_ID, MEAL5_ID));
    }

    @Test
    public void getWithUserNotFound() {
        assertThrowsNotFoundException(
                () -> service.getWithUser(NOT_FOUND_ID, MEAL3_ID));
    }
}
