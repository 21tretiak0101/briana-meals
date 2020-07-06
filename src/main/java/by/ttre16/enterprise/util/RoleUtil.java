package by.ttre16.enterprise.util;

import by.ttre16.enterprise.model.Role;

public class RoleUtil {
    public static final Integer ROLE_ADMIN_ID = 9998;

    public static final Integer ROLE_USER_ID = 9999;

    public static final String ROLE_ADMIN_NAME = "ADMIN";

    public static final String ROLE_USER_NAME = "USER";

    public static Role getRoleUser() {
        return new Role(ROLE_USER_ID, ROLE_USER_NAME);
    }

    public static Role getRoleAdmin() {
        return new Role(ROLE_ADMIN_ID, ROLE_ADMIN_NAME);
    }
}
