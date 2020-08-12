package by.ttre16.enterprise.repository.impl.jpa;

import by.ttre16.enterprise.annotation.QualifierRepository;
import by.ttre16.enterprise.model.User;
import by.ttre16.enterprise.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static by.ttre16.enterprise.util.profile.ProfileUtil.JPA;
import static java.util.Optional.ofNullable;
import static org.springframework.dao.support.DataAccessUtils.singleResult;

@Repository
@Profile(JPA)
@QualifierRepository(JpaUserRepository.class)
public class JpaUserRepository implements UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<User> getByEmail(String email) {
        List<User> users = entityManager
                .createNamedQuery(User.GET_BY_EMAIL, User.class)
                .setParameter("email", email)
                .getResultList();
        return ofNullable(singleResult(users));
    }

    @Override
    public Collection<User> getAll() {
        return entityManager.createNamedQuery(User.GET_ALL, User.class)
                .getResultList();
    }

    @Override
    public User save(User user) {
        if (user.isNew()) {
            entityManager.persist(user);
            return user;
        }
        return entityManager.merge(user);
    }

    @Override
    public Optional<User> get(Integer id) {
        return ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public boolean deleteById(Integer id) {
        return entityManager.createNamedQuery(User.DELETE)
                .setParameter("id", id)
                .executeUpdate() == 1;
    }

    @Override
    public Optional<User> getWithMeals(Integer id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> user = query.from(User.class);
        user.fetch("meals");
        query.select(user).where(cb.equal(user.get("id"), id));
        return entityManager.createQuery(query).getResultStream().findFirst();
    }
}
