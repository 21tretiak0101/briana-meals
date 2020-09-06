package by.ttre16.enterprise.controller.jsp;

import by.ttre16.enterprise.controller.AbstractUserController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static by.ttre16.enterprise.util.web.UrlUtil.ADMIN_JSP_URL;

@Controller
@RequestMapping(ADMIN_JSP_URL)
public class AdminJspController extends AbstractUserController {
    @GetMapping("/users")
    public String getAllUsers(Model model) {
        model.addAttribute("users", super.getAll());
        return "users";
    }
}

