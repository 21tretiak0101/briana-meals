package by.ttre16.enterprise.controller.jsp;

import by.ttre16.enterprise.model.Meal;
import by.ttre16.enterprise.service.MealService;
import by.ttre16.enterprise.security.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static by.ttre16.enterprise.util.ActionType.CREATE;
import static by.ttre16.enterprise.util.ActionType.UPDATE;
import static by.ttre16.enterprise.util.DateTimeUtil.atEndOfDayOrMax;
import static by.ttre16.enterprise.util.DateTimeUtil.atStartOfDayOrMin;
import static by.ttre16.enterprise.util.MealUtil.getMealsWithExcess;
import static by.ttre16.enterprise.util.ServletUtil.getMeal;
import static by.ttre16.enterprise.util.ServletUtil.getParameter;
import static by.ttre16.enterprise.util.ValidationUtil.checkNew;

@Controller
@RequestMapping("/meals")
public class JspMealController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final MealService service;
    private final SecurityUtil securityUtil;

    public JspMealController(MealService service, SecurityUtil securityUtil) {
        this.service = service;
        this.securityUtil = securityUtil;
    }

    @PostMapping("/save/{action}")
    public String save(@PathVariable String action,
            HttpServletRequest request) {
        Integer userId = securityUtil.getAuthUserId();
        Meal meal = getMeal(request);
        meal.getUser().setId(userId);
        log.info("Save meal: {}", meal);
        switch(action) {
            case CREATE:
                checkNew(meal);
                log.info("update {}", meal);
                service.save(userId, meal);
                break;
            case UPDATE:
                log.info("update {} with id={}", meal, meal.getId());
                service.update(meal, userId);
        }
        return "redirect:/meals";
    }

    @PostMapping("/delete/{mealId}")
    public String delete(@PathVariable Integer mealId) {
        Integer userId = securityUtil.getAuthUserId();
        log.info("Delete {}", mealId);
        service.delete(userId, mealId);
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
        List<Meal> mealsDateFiltered = service.getBetweenInclusive(
                atStartOfDayOrMin(startDate, startTime),
                atEndOfDayOrMax(endDate, endTime),
                userId);
        model.addAttribute("meals", getMealsWithExcess(
                mealsDateFiltered, startTime, endTime,
                securityUtil.getAuthUserCaloriesPerDay()));
        return "meals";
    }

    @GetMapping({"/edit/{action}/{id}", "/edit/{action}"})
    public String getEditMealPage(@PathVariable String action,
            @PathVariable(required = false) Integer id, Model model) {
        if (action.equals(UPDATE)) {
            Integer userId = securityUtil.getAuthUserId();
            model.addAttribute("meal", service.getOne(userId, id));
        } else {
            model.addAttribute("meal", null);
        }
        return "edit_meal";
    }
}
