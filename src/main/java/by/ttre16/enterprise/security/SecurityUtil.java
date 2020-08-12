package by.ttre16.enterprise.security;

import by.ttre16.enterprise.service.UserService;

public class SecurityUtil {
    private final UserService userService;
    private Integer authUserId;
    private Integer authUserCaloriesPerDay;
    private static final Integer TEST_ADMIN_ID = 2;

    public SecurityUtil(UserService userService) {
        this.userService = userService;
    }

    public void setAuthUserId(Integer authUserId) {
        this.authUserId = authUserId;
        setAuthUserCaloriesPerDay();
    }

    public void setAuthUserCaloriesPerDay() {
        this.authUserCaloriesPerDay = userService.get(authUserId)
                .getCaloriesPerDay();
    }

    /**
     * @return authenticated user ID
     */
    public Integer getAuthUserId() {
        return TEST_ADMIN_ID;
    }

    public Integer getAuthUserCaloriesPerDay() {
        return authUserCaloriesPerDay;
    }
}
