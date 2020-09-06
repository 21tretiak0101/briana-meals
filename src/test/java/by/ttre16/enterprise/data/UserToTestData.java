package by.ttre16.enterprise.data;

import by.ttre16.enterprise.controller.util.matcher.TestMatcher;
import by.ttre16.enterprise.dto.to.UserTo;
import by.ttre16.enterprise.model.User;
import by.ttre16.enterprise.service.util.AssertUtil;

import java.util.Arrays;

import static by.ttre16.enterprise.data.UserTestData.*;

public class UserToTestData {
    public static final TestMatcher<UserTo> USER_TO_TEST_MATCHER
            = new TestMatcher<>(UserTo.class);

    public static UserTo getNewUserTo() {
        User newUser = UserTestData.getNewUser();
        UserTo newUserTo = new UserTo();
        newUserTo.setPassword(newUser.getPassword());
        newUserTo.setEmail(newUser.getEmail());
        newUserTo.setName(newUser.getName());
        newUserTo.setCaloriesPerDay(newUser.getCaloriesPerDay());
        newUserTo.setRoles(newUser.getRoles());
        return newUserTo;
    }

    public static UserTo getNewAdminTo() {
        UserTo newAdminTo = getNewUserTo();
        newAdminTo.setRoles(Arrays.asList(ROLE_USER, ROLE_ADMIN));
        return newAdminTo;
    }

    public static void assertMatch(UserTo expected, UserTo actual) {
        AssertUtil.assertMatch(expected, actual);
    }

    public static UserTo getUserToWithInvalidPassword() {
        UserTo userTo = getNewUserTo();
        userTo.setPassword("");
        return userTo;
    }

    public static UserTo getUserToWithNonUniqueEmail() {
        UserTo userTo = getNewUserTo();
        userTo.setEmail(USERS.get(ADMIN_ID).getEmail());
        return userTo;
    }
}
