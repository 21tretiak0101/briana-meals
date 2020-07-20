package by.ttre16.enterprise.repository.impl.datajpa;

import by.ttre16.enterprise.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CrudMealRepository extends JpaRepository<Meal, Integer>,
        JpaSpecificationExecutor<Meal> {

    @Modifying
    @Query("delete from Meal m where m.user.id=:userId and m.id=:id")
    int delete(@Param("userId") Integer userId, @Param("id") Integer id);

    @Modifying
    @Query("delete from Meal m where m.user.id=:userId")
    int deleteAll(@Param("userId") Integer userId);

    @Query("select m from Meal m left join fetch User u on m.user.id = u.id" +
            " where u.id = ?1 and m.id = ?2")
    Optional<Meal> getWithUser(Integer userId, Integer id);

}
