package by.ttre16.enterprise.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Meal extends AbstractBaseEntity{
    private Integer calories;
    private LocalDateTime dateTime;
    private String description;

    public Meal() { }

    public Meal(Integer id, Integer calories,
                LocalDateTime dateTime, String description) {
        super(id);
        this.id = id;
        this.calories = calories;
        this.dateTime = dateTime;
        this.description = description;
    }

    public Meal(Meal meal) {
        this(meal.id, meal.calories, meal.dateTime, meal.description);
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
                "id=" + id +
                ", calories=" + calories +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
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
                Objects.equals(description, meal.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), calories, dateTime, description);
    }
}
