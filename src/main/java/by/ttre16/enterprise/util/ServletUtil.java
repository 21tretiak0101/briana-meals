package by.ttre16.enterprise.util;

import javax.servlet.http.HttpServletRequest;
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
}
