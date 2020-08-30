package by.ttre16.enterprise.data;

import by.ttre16.enterprise.controller.util.matcher.TestMatcher;
import by.ttre16.enterprise.dto.UserInfo;
import by.ttre16.enterprise.model.User;
import by.ttre16.enterprise.service.util.AssertUtil;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static by.ttre16.enterprise.data.UserTestData.USERS;

public class UserInfoTestData {
    public static final TestMatcher<UserInfo> USER_INFO_TEST_MATCHER =
            new TestMatcher<>(UserInfo.class);

    public static UserInfo getUserInfo(Integer id) {
        return new UserInfo(USERS.get(id));
    }

    public static UserInfo getUserInfo(User user) {
        return new UserInfo(user);
    }

    public static List<UserInfo> getAll() {
        return map(USERS.values());
    }

    public static List<UserInfo> map(Collection<User> users) {
        return users.stream()
                .map(UserInfo::new)
                .collect(Collectors.toList());
    }

    public static void assertMatch(UserInfo expected, UserInfo actual) {
        AssertUtil.assertMatch(expected, actual);
    }

    public static void assertMatch(Iterable<UserInfo> expected,
            Iterable<UserInfo> actual) {
        AssertUtil.assertMatch(expected, actual);
    }
}
