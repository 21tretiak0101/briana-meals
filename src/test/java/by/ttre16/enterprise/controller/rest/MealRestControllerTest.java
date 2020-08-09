package by.ttre16.enterprise.controller.rest;

import by.ttre16.enterprise.model.Meal;
import by.ttre16.enterprise.service.MealService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static by.ttre16.enterprise.controller.util.json.JsonUtil.*;
import static by.ttre16.enterprise.data.MealTestData.*;
import static by.ttre16.enterprise.data.TestData.ADMIN_ID;
import static by.ttre16.enterprise.util.web.UrlUtil.MEAL_REST_URL;
import static org.springframework.test.web.servlet
        .request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result
        .MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.
        MockMvcResultMatchers.*;

public class MealRestControllerTest extends AbstractRestControllerTest {

    @Autowired
    private MealService mealService;

    @Test
    public void getAll() throws Exception {
        perform(get(MEAL_REST_URL))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_TEST_MATCHER.contentJson(
                        MEALS.get(ADMIN_ID).values()))
                .andDo(print());
    }

    @Test
    public  void create() throws Exception {
        MvcResult response = perform(post(MEAL_REST_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(writeValue(getNew())))
                .andExpect(status().isCreated())
                .andDo(print()).andReturn();
        Meal savedMeal = readFromJson(response, Meal.class);
        Meal newMeal = getNew();
        Integer mid = savedMeal.getId();
        newMeal.setId(mid);
        assertMatch(newMeal, savedMeal);
        assertMatch(newMeal, mealService.getOne(ADMIN_ID, mid));
    }

    @Test
    public void update() throws Exception {
        perform(put(MEAL_REST_URL + "/" + MEAL5_ID)
            .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(getUpdated(ADMIN_ID, MEAL5_ID))))
        .andExpect(status().isNoContent())
        .andDo(print());
        assertMatch(getUpdated(ADMIN_ID, MEAL5_ID),
                mealService.getOne(ADMIN_ID, MEAL5_ID));
    }

    @Test
    public void deleteOne() throws Exception {
        perform(delete(MEAL_REST_URL + "/" + MEAL5_ID))
                .andExpect(status().isNoContent())
                .andDo(print());
        Map<Integer, Meal> adminMealMap = MEALS.get(ADMIN_ID);
        adminMealMap.remove(MEAL5_ID);
        assertMatch(adminMealMap.values(),
                mealService.getAllByUserId(ADMIN_ID));
    }
}
