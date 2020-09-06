package by.ttre16.enterprise.dto.mapper;

import by.ttre16.enterprise.dto.to.UserTo;
import by.ttre16.enterprise.model.User;
import org.mapstruct.*;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public interface UserEntityMapper extends EntityMapper<User, UserTo> {
    @Override
    @Mapping(target = "password", ignore = true)
    UserTo toDto(User user);

    @Override
    @Mapping(target = "registered", ignore = true)
    @Mapping(target = "meals", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    void updateEntityFromDto(@MappingTarget User user, UserTo userTo);
}
