package by.ttre16.enterprise.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

import static by.ttre16.enterprise.util.MealUtil.DEFAULT_CALORIES_PER_DAY;

@NamedQueries({
    @NamedQuery(name = User.GET_ALL,
            query = "select distinct u from User u join fetch u.roles "),
    @NamedQuery(name = User.GET_BY_EMAIL,
        query = "select u from User u " +
                "where u.email=:email "),
    @NamedQuery(name = User.DELETE,
        query = "delete from User u where u.id=:id"),
})
@Entity
@Table(name = "USERS", indexes = @Index(columnList = "email",
        name = "users_unique_email_idx", unique = true))
public class User extends AbstractNamedEntity {
    public final static String GET_ALL = "User.getAll";
    public final static String GET_BY_EMAIL = "User.getByEmail";
    public final static String DELETE = "User.delete";

    @Email
    @NotBlank
    @Max(value = 50)
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotEmpty
    @Size(min = 5, max = 255)
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @Column(name = "registered", columnDefinition = "timestamp default now()")
    private Date registered = new Date();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE})
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    @Column(name = "calories_per_day")
    private Integer caloriesPerDay = DEFAULT_CALORIES_PER_DAY;

    public User(Integer id, String name, String email, String password,
                boolean enabled, List<Role> roles) {
        this(id, name, email, password, enabled, new Date(), roles,
                DEFAULT_CALORIES_PER_DAY);
    }

    public User(Integer id, String name, String email, String password,
                boolean enabled, Date registered, List<Role> roles,
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

    public User(Integer id) {
        super(id);
    }

    public User() { }

    public void setRegistered(Date registered) {
        this.registered = registered;
    }

    public void setCaloriesPerDay(Integer caloriesPerDay) {
        this.caloriesPerDay = caloriesPerDay;
    }

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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
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
