package by.ttre16.enterprise.controller.jsp;

import by.ttre16.enterprise.model.Meal;
import by.ttre16.enterprise.service.MealService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import static by.ttre16.enterprise.data.MealTestData.*;
import static by.ttre16.enterprise.util.web.UrlUtil.MEAL_JSP_URL;
import static org.springframework.test.web.servlet.request
        .MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result
        .MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.
        MockMvcResultMatchers.*;

public class MealJspControllerTest extends AbstractJspControllerTest {
    @Autowired
    private MealService mealService;

    @Test
    public void delete() throws Exception {
        perform(post(MEAL_JSP_URL + "/delete/" + MEAL5_ID))
                .andExpect(redirectedUrl("/meals"))
                .andDo(print());
        ArrayList<Meal> adminMeals = new ArrayList<>();
        adminMeals.add(MEALS.get(ADMIN_ID).get(MEAL6_ID));
        adminMeals.add(MEALS.get(ADMIN_ID).get(MEAL7_ID));
        assertMatch(adminMeals, mealService.getAllByUserId(ADMIN_ID));
    }
}
