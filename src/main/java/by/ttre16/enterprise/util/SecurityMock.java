package by.ttre16.enterprise.util;

public class SecurityMock {
    private static final Integer TEST_ADMIN_ID = 2;

    /**
     * Method for testing jsp controllers
     * @return authenticated user ID
     */
    public static Integer getAuthUserId() {
        return TEST_ADMIN_ID;
    }
}
