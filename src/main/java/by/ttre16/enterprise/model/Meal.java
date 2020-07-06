package by.ttre16.enterprise.model;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.util.Objects.hash;

@Entity
@Table(name = "meals")
@NamedQueries({
    @NamedQuery(name = Meal.GET_ALL,
            query = "select m from Meal m where m.user.id=:userId"),
    @NamedQuery(name = Meal.DELETE_ALL,
            query = "delete from Meal m where m.user.id=:userId"),
    @NamedQuery(name = Meal.DELETE_ONE,
            query = "delete from Meal m " +
                    "where m.id=:mealId and m.user.id=:userId"),
})
public class Meal extends AbstractBaseEntity {
    public static final String GET_ALL = "Meal.getAll";
    public static final String DELETE_ONE = "Meal.deleteOne";
    public static final String DELETE_ALL = "Meal.deleteAll";

    @Range(min = 10, max = 5000)
    @NotNull
    @Column(name = "calories", nullable = false)
    private Integer calories;

    @NotNull
    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Size(min = 2, max = 120)
    @NotBlank
    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Meal() { }

    public Meal(Integer id, Integer calories, LocalDateTime dateTime,
                String description, User user) {
        super(id);
        this.id = id;
        this.calories = calories;
        this.dateTime = dateTime;
        this.description = description;
        this.user = user;
    }

    public Meal(Integer id, Integer calories, LocalDateTime dateTime,
                String description) {
        super(id);
        this.id = id;
        this.calories = calories;
        this.dateTime = dateTime;
        this.description = description;
    }

    public Meal(Meal meal) {
        this(meal.id, meal.calories, meal.dateTime, meal.description, meal.user);
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getCalories() {
        return calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "calories=" + calories +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", userId=" + user.getId() +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Meal meal = (Meal) o;
        return calories.equals(meal.calories) &&
                dateTime.equals(meal.dateTime) &&
                description.equals(meal.description) &&
                user.getId().equals(meal.user.id);
    }

    @Override
    public int hashCode() {
        return hash(super.hashCode(), calories, dateTime, description, user.id);
    }
}
