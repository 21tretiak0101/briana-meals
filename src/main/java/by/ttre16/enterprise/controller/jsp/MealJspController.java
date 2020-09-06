package by.ttre16.enterprise.controller.jsp;

import by.ttre16.enterprise.controller.AbstractMealController;
import by.ttre16.enterprise.dto.to.MealTo;
import by.ttre16.enterprise.model.Meal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static by.ttre16.enterprise.util.SecurityMock.getAuthUserId;
import static by.ttre16.enterprise.util.web.ActionType.CREATE;
import static by.ttre16.enterprise.util.web.ActionType.UPDATE;
import static by.ttre16.enterprise.util.web.ServletUtil.getMeal;
import static by.ttre16.enterprise.util.web.ServletUtil.getParameter;
import static by.ttre16.enterprise.util.web.UrlUtil.MEAL_JSP_URL;

@Controller
@RequestMapping(MEAL_JSP_URL)
public class MealJspController extends AbstractMealController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PostMapping("/save/{action}")
    public String save(@PathVariable String action,
            HttpServletRequest request) {
        Integer userId = getAuthUserId();
        Integer mealId = getParameter(request, "mealId", Integer::parseInt);
        Meal meal = getMeal(request);
        meal.getUser().setId(userId);
        switch(action) {
            case CREATE:
                super.create(meal, userId);
                break;
            case UPDATE:
                super.save(userId, meal, mealId);
        }
        return "redirect:/meals";
    }

    @PostMapping("/delete/{mealId}")
    public String delete(@PathVariable Integer mealId) {
        super.delete(getAuthUserId(), mealId);
        return "redirect:/meals";
    }

    @GetMapping
    public String getBetween(HttpServletRequest request, Model model) {
        Integer userId = getAuthUserId();
        LocalDate startDate = getParameter(request, "startDate",
                LocalDate::parse);
        LocalDate endDate = getParameter(request, "endDate",
                LocalDate::parse);
        LocalTime startTime = getParameter(request, "startTime",
                LocalTime::parse);
        LocalTime endTime = getParameter(request, "endTime",
                LocalTime::parse);
        log.info("Get between dates({} - {}) time({} - {}) for user {}",
                startDate, endDate, startTime, endTime, userId);

        List<MealTo> meals = super.getBetween(
                startDate,
                endDate,
                startTime,
                endTime,
                userId
        );

        model.addAttribute("meals", meals);
        return "meals";
    }

    @GetMapping({"/edit/{action}/{id}", "/edit/{action}"})
    public String getEditMealPage(@PathVariable String action,
            @PathVariable(required = false) Integer id, Model model) {
        if (action.equals(UPDATE)) {
            Integer userId = getAuthUserId();
            model.addAttribute("meal", super.get(userId, id));
        } else {
            model.addAttribute("meal", null);
        }
        return "edit_meal";
    }
}
