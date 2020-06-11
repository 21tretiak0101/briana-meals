package by.ttre16.enterprise.repository;

import java.util.Collection;
import java.util.Optional;

public interface CrudRepository<T, ID> {
    T save(T entity);

    boolean deleteById(ID id);

    Optional<T> getById(ID id);

    Collection<T> getAll();

    boolean existsById(ID id);

}
