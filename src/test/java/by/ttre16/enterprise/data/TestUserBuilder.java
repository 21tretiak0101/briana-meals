package by.ttre16.enterprise.data;

import by.ttre16.enterprise.model.Meal;
import by.ttre16.enterprise.model.Role;
import by.ttre16.enterprise.model.User;

import java.util.Date;
import java.util.List;

public final class TestUserBuilder {
    private Integer id;
    private String email;
    private String name;
    private String password;
    private boolean enabled;
    private Date registered = new Date();
    private List<Role> roles;
    private Integer caloriesPerDay = 2000;
    private List<Meal> meals;
    
    public static TestUserBuilder builder() {
        return new TestUserBuilder();
    }

    public User build() {
        return new User(
                id,
                name,
                email,
                password,
                enabled,
                registered,
                roles,
                caloriesPerDay,
                meals
        );
    }

    public TestUserBuilder email(String email) {
        this.email = email;
        return this;
    }

    public TestUserBuilder id(Integer id) {
        this.id = id;
        return this;
    }

    public TestUserBuilder name(String name) {
        this.name = name;
        return this;
    }
    public TestUserBuilder password(String password) {
        this.password = password;
        return this;
    }

    public TestUserBuilder enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public TestUserBuilder registered(Date registered) {
        this.registered = registered;
        return this;
    }

    public TestUserBuilder roles(List<Role> roles) {
        this.roles = roles;
        return this;
    }

    public TestUserBuilder caloriesPerDay(Integer caloriesPerDay) {
        this.caloriesPerDay = caloriesPerDay;
        return this;
    }

    public TestUserBuilder meals(List<Meal> meals) {
        this.meals = meals;
        return this;
    }
}
