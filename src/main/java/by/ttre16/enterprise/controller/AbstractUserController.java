package by.ttre16.enterprise.controller;

import by.ttre16.enterprise.model.Role;
import by.ttre16.enterprise.model.User;
import by.ttre16.enterprise.security.SecurityUtil;
import by.ttre16.enterprise.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static by.ttre16.enterprise.util.RoleUtil.ROLE_ADMIN_NAME;
import static by.ttre16.enterprise.util.ValidationUtil.assureIdConsistent;
import static by.ttre16.enterprise.util.ValidationUtil.checkNew;

public abstract class AbstractUserController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected UserService userService;

    @Autowired
    protected SecurityUtil securityUtil;

    public List<User> getAll() {
        log.info("Get all");
        return new ArrayList<>(userService.getAll());
    }

    public User get(Integer id) {
        log.info("Get {}", id);
        return userService.get(id);
    }

    public User create(User user) {
        log.info("Create {}", user);
        checkNew(user);
        return userService.create(user);
    }

    public void delete(Integer id) {
        log.info("Delete {}", id);
        userService.delete(id);
    }

    public void update(User user, Integer id) {
        log.info("Update {} with id={}", user, id);
        assureIdConsistent(user, id);
        userService.update(user);
    }

    public User getByMail(String email) {
        log.info("Get by email {}", email);
        return userService.getByEmail(email);
    }

    protected boolean isAdmin() {
        return userService.get(securityUtil.getAuthUserId())
                .getRoles().stream()
                .map(Role::getName)
                .anyMatch(roleName -> roleName.equals(ROLE_ADMIN_NAME));
    }
}
