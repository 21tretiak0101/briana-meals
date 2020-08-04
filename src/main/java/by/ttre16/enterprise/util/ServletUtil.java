package by.ttre16.enterprise.util;

import by.ttre16.enterprise.model.Meal;
import by.ttre16.enterprise.model.User;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.function.Function;

import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.isEmpty;

public class ServletUtil {
    public static <T> T getParameter(HttpServletRequest request, String key,
            Function<String, T> func) {
        String param = request.getParameter(key);
        return nonNull(param) && !isEmpty(param)
                ? func.apply(param)
                : null;
    }

    public static Meal getMeal(HttpServletRequest request) {
        Integer mealId = getParameter(request, "mealId", Integer::parseInt);
        Integer calories = getParameter(request, "calories", Integer::parseInt);
        String description = request.getParameter("description");
        LocalDateTime dateTime = getParameter(request, "dateTime",
                LocalDateTime::parse);
        return new Meal(mealId, calories, dateTime, description, new User());
    }
}
