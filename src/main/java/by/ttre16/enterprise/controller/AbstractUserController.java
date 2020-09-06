package by.ttre16.enterprise.controller;

import by.ttre16.enterprise.dto.mapper.UserEntityMapper;
import by.ttre16.enterprise.model.User;
import by.ttre16.enterprise.service.UserService;
import by.ttre16.enterprise.validation.UniqueEmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.ArrayList;
import java.util.List;

import static by.ttre16.enterprise.util.ValidationUtil.assureIdConsistent;
import static by.ttre16.enterprise.util.ValidationUtil.checkNew;

public abstract class AbstractUserController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    protected UniqueEmailValidator emailValidator;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    protected UserEntityMapper userMapper;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

    public List<User> getAll() {
        log.info("Get all");
        return new ArrayList<>(userService.getAll());
    }

    public User get(Integer id) {
        log.info("Get {}", id);
        return userService.get(id);
    }

    public User create(User user) {
        encodePassword(user);
        log.info("Create {}", user);
        checkNew(user);
        return userService.create(user);
    }

    public User createAdmin(User user) {
        encodePassword(user);
        log.info("Create {}", user);
        checkNew(user);
        return userService.createAsAdmin(user);
    }

    public void delete(Integer id) {
        log.info("Delete {}", id);
        userService.delete(id);
    }

    public void save(User user, Integer id) {
        log.info("Save {} with id={}", user, id);
        assureIdConsistent(user, id);
        userService.update(user);
    }

    public User getByMail(String email) {
        log.info("Get by email {}", email);
        return userService.getByEmail(email);
    }

    private void encodePassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }
}
