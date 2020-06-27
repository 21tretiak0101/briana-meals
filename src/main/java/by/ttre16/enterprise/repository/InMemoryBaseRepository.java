package by.ttre16.enterprise.repository;

import by.ttre16.enterprise.model.AbstractBaseEntity;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.nonNull;

public class InMemoryBaseRepository <T extends AbstractBaseEntity> {
    private static final AtomicInteger counter = new AtomicInteger(0);

    private final Map<Integer, T> map = new ConcurrentHashMap<>();

    public T save(T entry) {
        if (entry.isNew()) {
            entry.setId(counter.incrementAndGet());
            map.put(entry.getId(), entry);
            return entry;
        }
        return map.computeIfPresent(entry.getId(), (id, oldT) -> entry);
    }

    public boolean delete(Integer id) {
        return nonNull(map.remove(id));
    }

    public Optional<T> get(Integer id) {
        return Optional.ofNullable(map.get(id));
    }

    public Collection<T> getCollection() {
        return map.values();
    }
}
