package by.ttre16.enterprise.dto;

import java.time.LocalDateTime;

public class MealTo {
    private Integer id;
    private final Integer calories;
    private final LocalDateTime dateTime;
    private final String description;
    private boolean excess;

    public MealTo(Integer id, Integer calories, LocalDateTime dateTime,
            String description, boolean excess) {
        this.id = id;
        this.calories = calories;
        this.dateTime = dateTime;
        this.description = description;
        this.excess = excess;
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

    public String getDescription() {
        return description;
    }

    public void setExcess(boolean excess) {
        this.excess = excess;
    }

    public boolean isExcess() {
        return excess;
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "id=" + id +
                ", calories=" + calories +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", excess=" + excess +
                '}';
    }
}
