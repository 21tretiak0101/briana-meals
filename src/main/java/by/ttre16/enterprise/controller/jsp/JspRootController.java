package by.ttre16.enterprise.controller.jsp;

import by.ttre16.enterprise.security.SecurityUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import static by.ttre16.enterprise.util.ServletUtil.getParameter;
import static org.slf4j.LoggerFactory.getLogger;

@Controller
@RequestMapping("/auth")
public class JspRootController {
    private static final Logger log = getLogger(JspRootController.class);
    private final SecurityUtil securityUtil;

    @Autowired
    public JspRootController(SecurityUtil securityUtil) {
        this.securityUtil = securityUtil;
    }

    @PostMapping
    public String setUser(HttpServletRequest request) {
        Integer userId = getParameter(request, "id", Integer::parseInt);
       securityUtil.setAuthUserId(userId);
        log.info("Set user: {}", userId);
        return "redirect:meals";
    }
}
