package by.ttre16.enterprise.controller.jsp;

import by.ttre16.enterprise.model.Role;
import by.ttre16.enterprise.security.SecurityUtil;
import by.ttre16.enterprise.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.AccessControlException;

import static by.ttre16.enterprise.util.RoleUtil.ROLE_ADMIN_NAME;

@Controller
@RequestMapping("/admin")
public class JspAdminController {
    private final UserService userService;
    private final SecurityUtil securityUtil;

    @Autowired
    public JspAdminController(UserService userService,
            SecurityUtil securityUtil) {
        this.userService = userService;
        this.securityUtil = securityUtil;
    }

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        if (isAdmin()) {
            model.addAttribute("users", userService.getAll());
            return "users";
        } else {
            throw new AccessControlException(
                    "The user doesn't have administrator rights");
        }
    }

    private boolean isAdmin() {
        return userService.get(securityUtil.getAuthUserId())
                .getRoles().stream()
                .map(Role::getName)
                .anyMatch(roleName -> roleName.equals(ROLE_ADMIN_NAME));
    }
}
