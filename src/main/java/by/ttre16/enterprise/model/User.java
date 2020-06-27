package by.ttre16.enterprise.model;

import java.util.Date;
import java.util.Set;

import static by.ttre16.enterprise.util.MealUtil.DEFAULT_CALORIES_PER_DAY;

public class User extends AbstractNamedEntity {
    private String email;
    private String password;
    private boolean enabled = true;
    private Date registered = new Date();
    private Set<Role> roles;

    public void setRegistered(Date registered) {
        this.registered = registered;
    }

    public void setCaloriesPerDay(Integer caloriesPerDay) {
        this.caloriesPerDay = caloriesPerDay;
    }

    private Integer caloriesPerDay = DEFAULT_CALORIES_PER_DAY;

    public User(Integer id, String name) {
        super(id, name);
    }

    public User(Integer id, String name, String email, String password,
            boolean enabled, Set<Role> roles) {
        this(id, name, email, password, enabled, new Date(), roles,
                DEFAULT_CALORIES_PER_DAY);
    }

    public User(Integer id, String name, String email, String password,
            boolean enabled, Date registered, Set<Role> roles,
            Integer caloriesPerDay) {
        super(id, name);
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.registered = registered;
        this.roles = roles;
        this.caloriesPerDay = caloriesPerDay;
    }

    public User(User user) {
        this(user.id, user.name, user.email, user.password, user.enabled,
                 user.registered ,user.roles, user.caloriesPerDay);
    }

    public User() { }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getRegistered() {
        return registered;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Integer getCaloriesPerDay() {
        return caloriesPerDay;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", enabled=" + enabled +
                ", registered=" + registered +
                ", roles=" + roles +
                ", caloriesPerDay=" + caloriesPerDay +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
