package by.ttre16.enterprise.servlet;

import by.ttre16.enterprise.configuration.ApplicationConfiguration;
import by.ttre16.enterprise.controller.MealRestController;
import by.ttre16.enterprise.dto.MealTo;
import by.ttre16.enterprise.model.Meal;
import by.ttre16.enterprise.model.User;
import org.slf4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static by.ttre16.enterprise.util.ActionType.*;
import static by.ttre16.enterprise.util.ProfileUtil.DATA_JPA;
import static by.ttre16.enterprise.util.ProfileUtil.DEVELOPMENT;
import static by.ttre16.enterprise.util.ServletUtil.getParameter;
import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private MealRestController controller;
    private static final Logger log = getLogger(MealServlet.class);
    private AnnotationConfigApplicationContext context;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles(DEVELOPMENT, DATA_JPA);
        context.register(ApplicationConfiguration.class);
        context.refresh();
        controller = context.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        context.close();
        super.destroy();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        LocalDate startDate = getParameter(req, "startDate",
                LocalDate::parse);
        LocalDate endDate = getParameter(req, "endDate",
                LocalDate::parse);
        LocalTime startTime = getParameter(req, "startTime",
                LocalTime::parse);
        LocalTime endTime = getParameter(req, "endTime",
                LocalTime::parse);
        List<MealTo> meals = controller
                .getBetween(startDate, endDate, startTime, endTime);
        req.setAttribute("meals", meals);
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        req.setCharacterEncoding("UTF-8");
        Integer userId = getParameter(req, "userId", Integer::parseInt);
        Integer mealId = getParameter(req, "mealId", Integer::parseInt);
        Integer calories = getParameter(req, "calories", Integer::parseInt);
        String description = req.getParameter("description");
        LocalDateTime dateTime = getParameter(req, "dateTime",
                LocalDateTime::parse);
        Meal meal = new Meal(mealId, calories, dateTime, description,
                new User(userId));
        String action = req.getParameter("action");
        switch (action) {
            case CREATE:
                controller.create(meal);
                break;
            case UPDATE:
                controller.update(meal, mealId);
                break;
            case DELETE:
                controller.delete(mealId);
                break;
            default:
                log.warn("Uncaught action: {}", action);
        }
        resp.sendRedirect("meals");
    }
}
