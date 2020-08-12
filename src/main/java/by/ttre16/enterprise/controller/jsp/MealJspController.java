package by.ttre16.enterprise.controller.jsp;

import by.ttre16.enterprise.controller.AbstractMealController;
import by.ttre16.enterprise.dto.MealTo;
import by.ttre16.enterprise.model.Meal;
import by.ttre16.enterprise.security.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static by.ttre16.enterprise.util.web.ActionType.CREATE;
import static by.ttre16.enterprise.util.web.ActionType.UPDATE;
import static by.ttre16.enterprise.util.web.ServletUtil.getMeal;
import static by.ttre16.enterprise.util.web.ServletUtil.getParameter;
import static by.ttre16.enterprise.util.web.UrlUtil.MEAL_JSP_URL;

@Controller
@RequestMapping(MEAL_JSP_URL)
public class MealJspController extends AbstractMealController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final SecurityUtil securityUtil;

    @Autowired
    public MealJspController(SecurityUtil securityUtil) {
        this.securityUtil = securityUtil;
    }

    @PostMapping("/save/{action}")
    public String save(@PathVariable String action,
            HttpServletRequest request) {
        Integer userId = securityUtil.getAuthUserId();
        Integer mealId = getParameter(request, "mealId", Integer::parseInt);
        Meal meal = getMeal(request);
        meal.getUser().setId(userId);
        switch(action) {
            case CREATE:
                super.create(meal, userId);
                break;
            case UPDATE:
                super.update(userId, meal, mealId);
        }
        return "redirect:/meals";
    }

    @PostMapping("/delete/{mealId}")
    public String delete(@PathVariable Integer mealId) {
        super.delete(securityUtil.getAuthUserId(), mealId);
        return "redirect:/meals";
    }

    @GetMapping
    public String getBetween(HttpServletRequest request, Model model) {
        Integer userId = securityUtil.getAuthUserId();
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

        List<MealTo> meals =
                super.getBetween(startDate, endDate, startTime, endTime, userId,
                        securityUtil.getAuthUserCaloriesPerDay());

        model.addAttribute("meals", meals);
        return "meals";
    }

    @GetMapping({"/edit/{action}/{id}", "/edit/{action}"})
    public String getEditMealPage(@PathVariable String action,
            @PathVariable(required = false) Integer id, Model model) {
        if (action.equals(UPDATE)) {
            Integer userId = securityUtil.getAuthUserId();
            model.addAttribute("meal", super.get(userId, id));
        } else {
            model.addAttribute("meal", null);
        }
        return "edit_meal";
    }
}
