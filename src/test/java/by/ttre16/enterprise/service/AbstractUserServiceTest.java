package by.ttre16.enterprise.service;

import by.ttre16.enterprise.model.Meal;
import by.ttre16.enterprise.model.User;
import by.ttre16.enterprise.service.util.MealTestData;
import by.ttre16.enterprise.util.exception.NotFoundException;

import org.junit.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static by.ttre16.enterprise.service.util.MealTestData.MEALS;
import static by.ttre16.enterprise.service.util.UserTestData.*;
import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertThrows;
import static org.slf4j.LoggerFactory.getLogger;

public abstract class AbstractUserServiceTest extends AbstractServiceTest {
    private static final Logger log = getLogger("result");

    @Autowired
    private UserService service;

    @Autowired
    private CacheManager cacheManager;

    @Before
    public void setUp() {
        requireNonNull(cacheManager.getCache("users")).clear();
    }

    @Test
    public void create() {
        User created = service.create(getNew());
        User newUser = getNew();
        Integer uid = created.getId();
        newUser.setId(uid);
        assertMatch(newUser, created);
        assertMatch(newUser, service.get(uid));
    }

    @Test
    public void createWithDuplicateEmail() {
        User oldUser = USERS.get(USER_ID);
        User duplicate = getNew();
        duplicate.setEmail(oldUser.getEmail());
        assertThrows(DataAccessException.class,
                () -> service.create(duplicate));
    }

    @Test
    public void delete() {
        Integer uid = USER_ID;
        service.delete(uid);
        assertThrows(NotFoundException.class, () -> service.get(uid));
    }

    @Test
    @Transactional(readOnly = true)
    public void get() {
        Integer uid = ADMIN_ID;
        User user = service.get(uid);
        log.info("get one user: {}", user);
        assertMatch(USERS.get(uid), user);
    }

    @Test
    public void getByEmail() {
        User expected = USERS.get(ADMIN_ID);
        User user = service.getByEmail(expected.getEmail());
        assertMatch(expected, user);
    }

    @Test
    @Transactional(readOnly = true)
    public void getAll() {
        List<User> users = service.getAll();
        users.forEach(u -> log.info("{}\n", u));
        assertMatch(USERS.values(), users);
    }

    @Test
    public void update() {
        service.update(getUpdated(USER_ID));
        assertMatch(getUpdated(USER_ID), service.get(USER_ID));
    }

    @Test
    public void getByIdNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND_ID));
    }

    @Test
    public void getByEmailNotFound() {
        assertThrows(NotFoundException.class,
                () -> service.getByEmail(NOT_FOUND_EMAIL));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class,
                () -> service.delete(NOT_FOUND_ID));
    }

    @Test
    @Transactional(readOnly = true)
    public void getWithMeals() {
        Collection<Meal> expectedMeals = MEALS.get(USER_ID).values();
        User user = service.getWithMeals(USER_ID);
        log.info("{}", user);
        MealTestData.assertMatch(expectedMeals, user.getMeals());
    }

    @Test
    public void getWithMealsNotFound() {
        assertThrows(NotFoundException.class,
                () -> service.getWithMeals(NOT_FOUND_ID));
    }
}
