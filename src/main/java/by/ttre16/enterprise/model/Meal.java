package by.ttre16.enterprise.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Meal extends AbstractBaseEntity{
    private final Integer calories;
    private final LocalDateTime dateTime;
    private final String description;

    public Meal(Integer id, Integer calories,
                LocalDateTime dateTime, String description) {
        super(id);
        this.id = id;
        this.calories = calories;
        this.dateTime = dateTime;
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
}
