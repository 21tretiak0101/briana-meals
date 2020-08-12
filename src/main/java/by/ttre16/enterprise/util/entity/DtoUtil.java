package by.ttre16.enterprise.util.entity;

import by.ttre16.enterprise.dto.MealTo;
import by.ttre16.enterprise.model.Meal;

public class DtoUtil {
    public static MealTo convertToDto(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getCalories(), meal.getDateTime(),
                meal.getDescription(), excess);
    }

    public static Meal convertToModel(MealTo mealTo) {
        return new Meal(mealTo.getId(), mealTo.getCalories(),
                mealTo.getDateTime(), mealTo.getDescription());
    }
}
