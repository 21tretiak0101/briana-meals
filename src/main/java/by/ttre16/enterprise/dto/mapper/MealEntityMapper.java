package by.ttre16.enterprise.dto.mapper;

import by.ttre16.enterprise.dto.to.MealTo;
import by.ttre16.enterprise.model.Meal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MealEntityMapper extends EntityMapper<Meal, MealTo> {
    @Override
    @Mapping(target = "excess", defaultValue = "true")
    @Mapping(target = "userId", source = "user.id")
    MealTo toDto(Meal user);

    @Override
    @Mapping(target = "user", ignore = true)
    void updateEntityFromDto(@MappingTarget Meal meal, MealTo mealTo);
}
