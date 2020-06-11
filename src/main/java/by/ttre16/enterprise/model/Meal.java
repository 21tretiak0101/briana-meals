package by.ttre16.enterprise.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Meal {
    private Integer id;
    private final Integer calories;
    private final LocalDateTime dateTime;
    private final String description;

    public Meal(Integer id, Integer calories, LocalDateTime dateTime,
                String description) {
        this.id = id;
        this.calories = calories;
        this.dateTime = dateTime;
        this.description = description;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
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

    public boolean isNew() {
        return id == null;
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
