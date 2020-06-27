package by.ttre16.enterprise.service;

import by.ttre16.enterprise.model.User;
import by.ttre16.enterprise.util.exception.NotFoundException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.util.List;

import static by.ttre16.enterprise.service.util.UserTestData.*;
import static org.junit.Assert.assertThrows;

public class UserServiceTest extends AbstractServiceTest {

    @Autowired
    private UserService service;

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
        User duplicate = new User();
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
    public void get() {
        Integer uid = USER_ID;
        User user = service.get(uid);
        System.out.println(user);
        assertMatch(USERS.get(uid), user);
    }

    @Test
    public void getByEmail() {
        User expected = USERS.get(ADMIN_ID);
        User user = service.getByEmail(expected.getEmail());
        assertMatch(expected, user);
    }

    @Test
    public void getAll() {
        List<User> users = service.getAll();
        System.out.println(users);
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
}
