package by.ttre16.enterprise.util;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Function;

import static java.util.Objects.nonNull;

public class ServletUtil {
    public static <T> T getParameter(HttpServletRequest request, String key,
                                     Function<String, T> func) {
        String param = request.getParameter(key);
        return nonNull(param) && !param.isEmpty()
                ? func.apply(param)
                : null;
    }
}
