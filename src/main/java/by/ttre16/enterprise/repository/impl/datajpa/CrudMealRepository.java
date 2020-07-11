package by.ttre16.enterprise.repository.impl.datajpa;

import by.ttre16.enterprise.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CrudMealRepository extends JpaRepository<Meal, Integer>,
        JpaSpecificationExecutor<Meal> {

    @Modifying
    @Query("delete from Meal m where m.user.id=:userId and m.id=:id")
    int delete(@Param("userId") Integer userId, @Param("id") Integer id);

    @Modifying
    @Query("delete from Meal m where m.user.id=:userId")
    int deleteAll(@Param("userId") Integer userId);
}
