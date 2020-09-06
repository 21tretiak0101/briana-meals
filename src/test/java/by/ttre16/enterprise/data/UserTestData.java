package by.ttre16.enterprise.data;

import by.ttre16.enterprise.model.Role;
import by.ttre16.enterprise.model.User;
import by.ttre16.enterprise.service.util.AssertUtil;

import java.util.*;

import static by.ttre16.enterprise.util.RoleUtil.getRoleAdmin;
import static by.ttre16.enterprise.util.RoleUtil.getRoleUser;
import static java.util.Collections.emptyList;

public class UserTestData extends TestData {
    public static final Map<Integer, User> USERS = new HashMap<>();
    public static final String NOT_FOUND_EMAIL = "mail@mail.com";
    public static final Role ROLE_USER = getRoleUser();
    public static final Role ROLE_ADMIN = getRoleAdmin();

    static {
        USERS.put(USER_ID, TestUserBuilder.builder()
                .id(USER_ID)
                .name("simple_user")
                .email("test@gmail.com")
                .password("$2y$10$TczuFGRWTfbOCmSYktMqne99oIAYbn.8QZv3Id32/aZdBLdDo7Wfu")
                .roles(Collections.singletonList(ROLE_USER))
                .enabled(true)
                .build()
        );

        USERS.put(ADMIN_ID, TestUserBuilder.builder()
                .id(ADMIN_ID)
                .name("admin")
                .email("admin@mail.com")
                .password("$2y$12$TCwbCOGqkSOqzDvXjrewYOuR6yeIjPieeBNm4D56IsujhF6gaEIh6")
                .roles(Arrays.asList(ROLE_USER, ROLE_ADMIN))
                .enabled(true)
                .build()
        );
    }

    public static User getNewUser() {
        return TestUserBuilder.builder()
                .id(null)
                .name("test_user")
                .email("test_user12@gmail.com")
                .password("hello-password")
                .enabled(true)
                .caloriesPerDay(1230)
                .roles(Collections.singletonList(ROLE_USER))
                .meals(emptyList())
                .build();
    }

    public static User getNewAdmin() {
        User newAdmin = getNewUser();
        newAdmin.setRoles(Arrays.asList(ROLE_USER, ROLE_ADMIN));
        return newAdmin;
    }

    public static User getUpdated(Integer id) {
        User updated = new User(USERS.get(id));
        updated.setName("updated_user");
        updated.setCaloriesPerDay(330);
        return updated;
    }

    public static void assertMatch(User expected, User actual) {
        AssertUtil.assertMatch(expected, actual, "registered", "meals");
    }

    public static void assertMatch(Iterable<User> expected,
            Iterable<User> actual) {
        AssertUtil.assertMatch(expected, actual, "registered", "meals");
    }
}

