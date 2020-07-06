package by.ttre16.enterprise.model;

import javax.persistence.*;
import java.util.Objects;

@MappedSuperclass
public abstract class AbstractBaseEntity {
    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "global_seq",
            initialValue = 100000, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "sequence")
    protected Integer id;

    public AbstractBaseEntity(Integer id) {
        this.id = id;
    }

    public AbstractBaseEntity() { }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isNew() {
        return id == null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractBaseEntity that = (AbstractBaseEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
