package by.ttre16.enterprise.servlet;

import by.ttre16.enterprise.dto.MealTo;
import by.ttre16.enterprise.model.Meal;
import by.ttre16.enterprise.service.MealService;
import org.slf4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static by.ttre16.enterprise.util.ActionType.*;
import static by.ttre16.enterprise.util.MealUtil.DEFAULT_CALORIES_PER_DAY;
import static by.ttre16.enterprise.util.MealUtil.getMealsWithExcess;
import static by.ttre16.enterprise.util.ServletUtil.getParameter;
import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private MealService service;
    private static final Logger log = getLogger(MealServlet.class);

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.service = new MealService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<MealTo> meals = getMealsWithExcess(service.getAll(),
                LocalTime.MIN, LocalTime.MAX, DEFAULT_CALORIES_PER_DAY);
        req.setAttribute("meals", meals);
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        req.setCharacterEncoding("UTF-8");
        Integer id = getParameter(req, "id", Integer::parseInt);
        Integer calories = getParameter(req, "calories", Integer::parseInt);
        String description = req.getParameter("description");
        LocalDateTime dateTime = getParameter(req, "dateTime",
                LocalDateTime::parse);
        Meal meal = new Meal(id, calories, dateTime, description);
        String action = req.getParameter("action");
        switch (action) {
            case CREATE:
            case UPDATE:
                service.save(meal);
                break;
            case DELETE:
                service.delete(meal.getId());
                break;
            default:
                log.warn("Uncaught action: {}", action);
        }
        resp.sendRedirect("meals");
    }
}
