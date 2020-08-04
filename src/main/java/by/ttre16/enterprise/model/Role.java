package by.ttre16.enterprise.model;

import javax.persistence.*;

@Entity
@Table(name = "ROLES")
public class Role extends AbstractNamedEntity {
    public Role() { }

    public Role(Integer id, String name) {
        super(id, name);
    }

    @Override
    public String toString() {
        return name;
    }
}
