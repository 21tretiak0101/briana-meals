package by.ttre16.enterprise.security;

import by.ttre16.enterprise.service.UserService;

public class SecurityUtil {
    private final UserService userService;
    private Integer authUserId;
    private Integer authUserCaloriesPerDay;

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

    public Integer getAuthUserId() {
        return authUserId;
    }

    public Integer getAuthUserCaloriesPerDay() {
        return authUserCaloriesPerDay;
    }
}
