package by.ttre16.enterprise.controller.rest;

import by.ttre16.enterprise.data.MealToTestData;
import by.ttre16.enterprise.dto.mapper.MealEntityMapper;
import by.ttre16.enterprise.dto.to.MealTo;
import by.ttre16.enterprise.model.Meal;
import by.ttre16.enterprise.service.MealService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static by.ttre16.enterprise.controller.util.json.JsonUtil.*;
import static by.ttre16.enterprise.data.MealTestData.*;
import static by.ttre16.enterprise.data.MealToTestData.MEAL_TO_TEST_MATCHER;
import static by.ttre16.enterprise.data.TestData.ADMIN_ID;
import static by.ttre16.enterprise.util.web.UrlUtil.MEAL_REST_URL;
import static org.springframework.test.web.servlet
        .request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result
        .MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.
        MockMvcResultMatchers.*;

@Transactional
public class MealRestControllerTest extends AbstractRestControllerTest {

    @Autowired
    private MealService mealService;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private MealEntityMapper mealMapper;

    @Override
    public Integer getTestUserId() {
        return ADMIN_ID;
    }

    @Test
    public void getAll() throws Exception {
        perform(get(MEAL_REST_URL)
                .header(authorizationHeader, bearerToken))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_TO_TEST_MATCHER.contentJson(
                        MEALS.get(getTestUserId()).values().stream()
                                .map(meal -> mealMapper.toDto(meal))
                                .collect(Collectors.toList()))
                )
                .andDo(print());
    }

    @Test
    public void create() throws Exception {
        MvcResult response = perform(post(MEAL_REST_URL)
                .header(authorizationHeader, bearerToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(writeValue(mealMapper.toDto(getNew()))))
                .andExpect(status().isCreated())
                .andDo(print()).andReturn();
        MealTo savedMeal = readFromJson(response, MealTo.class);
        MealTo newMeal = mealMapper.toDto(getNew());
        newMeal.setId(savedMeal.getId());
        newMeal.setUserId(savedMeal.getUserId());
        MealToTestData.assertMatch(newMeal, savedMeal);
    }

    @Test
    public void update() throws Exception {
        MealTo updated = mealMapper.toDto(
                getUpdated(getTestUserId(), MEAL5_ID)
        );
        perform(put(MEAL_REST_URL + "/" + MEAL5_ID)
                .header(authorizationHeader, bearerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(
                getUpdated(getTestUserId(), MEAL5_ID),
                mealService.getOne(getTestUserId(), MEAL5_ID)
        );
    }

    @Test
    public void deleteOne() throws Exception {
        perform(delete(MEAL_REST_URL + "/" + MEAL5_ID)
                .header(authorizationHeader, bearerToken))
                .andExpect(status().isNoContent())
                .andDo(print());
        ArrayList<Meal> adminMeals = new ArrayList<>();
        adminMeals.add(MEALS.get(getTestUserId()).get(MEAL6_ID));
        adminMeals.add(MEALS.get(getTestUserId()).get(MEAL7_ID));
        assertMatch(adminMeals, mealService.getAllByUserId(getTestUserId()));
    }
}
