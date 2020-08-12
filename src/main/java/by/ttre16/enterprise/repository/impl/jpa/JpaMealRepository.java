package by.ttre16.enterprise.repository.impl.jpa;

import by.ttre16.enterprise.annotation.QualifierRepository;
import by.ttre16.enterprise.model.Meal;
import by.ttre16.enterprise.model.User;
import by.ttre16.enterprise.repository.MealRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import static by.ttre16.enterprise.util.profile.ProfileUtil.JPA;
import static java.util.Optional.*;

@Repository
@Profile(JPA)
@QualifierRepository(JpaMealRepository.class)
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<Meal> getAll(Integer userId) {
        return entityManager.createNamedQuery(Meal.GET_ALL, Meal.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public Meal save(Integer userId, Meal meal) {
        meal.setUser(entityManager.getReference(User.class, userId));
        if (meal.isNew()) {
            entityManager.persist(meal);
            return meal;
        } else if (getOne(userId, meal.getId()).isPresent()) {
            return entityManager.merge(meal);
        }
        return null;
    }

    @Override
    public boolean deleteOne(Integer userId, Integer mealId) {
        return entityManager.createNamedQuery(Meal.DELETE_ONE)
                .setParameter("mealId", mealId)
                .setParameter("userId", userId)
                .executeUpdate() == 1;
    }

    @Override
    public Optional<Meal> getOne(Integer userId, Integer mealId) {
        return ofNullable(entityManager
                .find(Meal.class, mealId))
                .filter(m -> m.getUser().getId().equals(userId));
    }

    @Override
    public boolean deleteAll(Integer userId) {
        return entityManager.createNamedQuery(Meal.DELETE_ALL)
                .setParameter("userId", userId)
                .executeUpdate() != 0;
    }

    @Override
    public Optional<Meal> getWithUser(Integer userId, Integer id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Meal> query = cb.createQuery(Meal.class);
        Root<Meal> meal = query.from(Meal.class);
        meal.fetch("user");
        query.select(meal)
                .where(cb.and(
                        cb.equal(meal.get("user").get("id"), userId),
                        cb.equal(meal.get("id"), id)
                ));
        return entityManager.createQuery(query).getResultStream().findFirst();
    }

    @Override
    public Collection<Meal> getBetweenHalfOpen(LocalDateTime startDateTime,
     LocalDateTime endDateTime, Integer userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Meal> query = cb.createQuery(Meal.class);
        Root<Meal> meal = query.from(Meal.class);
        query.select(meal)
            .where(cb.and(
                cb.equal(meal.get("user").get("id"), userId),
                cb.greaterThanOrEqualTo(meal.get("dateTime"), startDateTime),
        cb.lessThan(meal.get("dateTime"), endDateTime)))
            .orderBy(cb.desc(meal.get("dateTime")));
        return entityManager.createQuery(query).getResultList();
    }
}
