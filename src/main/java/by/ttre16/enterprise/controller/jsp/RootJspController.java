package by.ttre16.enterprise.controller.jsp;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import static by.ttre16.enterprise.util.web.ServletUtil.getParameter;
import static by.ttre16.enterprise.util.web.UrlUtil.ROOT_JSP_URL;
import static org.slf4j.LoggerFactory.getLogger;

@Controller
@RequestMapping(ROOT_JSP_URL)
public class RootJspController {
    private static final Logger log = getLogger(RootJspController.class);

    @PostMapping
    public String setUser(HttpServletRequest request) {
        Integer userId = getParameter(request, "id", Integer::parseInt);
        log.info("Set user: {}", userId);
        return "redirect:meals";
    }
}
