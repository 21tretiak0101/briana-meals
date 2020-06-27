package by.ttre16.enterprise.service.util;

import by.ttre16.enterprise.model.Role;
import by.ttre16.enterprise.model.User;

import java.util.*;

public class UserTestData extends TestData {
    public static Map<Integer, User> USERS = new HashMap<>();
    public static String NOT_FOUND_EMAIL = "mail@mail.com";

    static {
        USERS.put(USER_ID, new User(1, "simple_user", "test@gmail.com",
                "password", true, EnumSet.of(Role.ADMIN)));
        USERS.put(ADMIN_ID, new User(2, "admin", "admin@mail.com",
                "password88-2", true, EnumSet.of(Role.ADMIN, Role.USER)));
    }

    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPass", false,
                new Date(), EnumSet.of(Role.USER), 1230);
    }

    public static User getUpdated(Integer id) {
        User updated = new User(USERS.get(id));
        updated.setName("UpdatedName");
        updated.setCaloriesPerDay(330);
        return updated;
    }

    public static void assertMatch(User expected, User actual) {
        AssertUtil.assertMatch(expected, actual, "roles", "registered");
    }

    public static void assertMatch(Iterable<User> expected,
            Iterable<User> actual) {
        AssertUtil.assertMatch(expected, actual, "registered", "roles");
    }
}

