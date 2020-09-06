package by.ttre16.enterprise.dto.mapper;

public interface EntityMapper<E, D> {
    D toDto(E dto);

    E toEntity(D entity);

    void updateEntityFromDto(E entity, D dto);
}
