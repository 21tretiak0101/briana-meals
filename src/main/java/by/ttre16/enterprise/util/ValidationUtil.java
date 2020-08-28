package by.ttre16.enterprise.util;

import by.ttre16.enterprise.exception.IllegalRequestDataException;
import by.ttre16.enterprise.model.AbstractBaseEntity;
import by.ttre16.enterprise.exception.NotFoundException;

import static java.util.Objects.nonNull;

public class ValidationUtil {
    public static <T> T checkNotFoundWithId(T object, Integer id) {
        checkNotFoundWithId(nonNull(object), id);
        return object;
    }

    public static void checkNotFoundWithId(boolean found, Integer id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(nonNull(object), msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with: " + msg);
        }
    }

    public static void checkNew(AbstractBaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalRequestDataException(
                    entity + " must be new (id = null)");
        }
    }

    public static void assureIdConsistent(AbstractBaseEntity entity, Integer id) {
        if (entity.isNew()) {
            entity.setId(id);
        } else if (!entity.getId().equals(id)) {
            throw new IllegalRequestDataException(
                    entity + " must be with id: " + id);
        }
    }
}
