package by.ttre16.enterprise.repository.impl.datajpa;

import by.ttre16.enterprise.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CrudUserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Modifying
    @Query("delete from User u where u.id=:id")
    int delete(@Param("id") Integer id);

    @EntityGraph(attributePaths = "meals")
    @Query("select u from User u where u.id=:id")
    Optional<User> getWithMeals(@Param("id") Integer id);
}
