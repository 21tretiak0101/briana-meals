package by.ttre16.enterprise.util.web;

public class UrlUtil {
    private static final String BASE_REST_URL = "/api/v1/";

    private static final String BASE_JSP_URL = "/jsp/v1/";

    public static final String PROFILE_REST_URL = BASE_REST_URL + "profile";

    public static final String ADMIN_REST_URL = BASE_REST_URL + "admin";

    public static final String MEAL_REST_URL = BASE_REST_URL + "meal";

    public static final String ROOT_JSP_URL = BASE_JSP_URL + "auth";

    public static final String PROFILE_JSP_URL = BASE_JSP_URL + "profile";

    public static final String ADMIN_JSP_URL = BASE_JSP_URL + "admin/users";

    public static final String MEAL_JSP_URL = BASE_JSP_URL + "meal";
}
