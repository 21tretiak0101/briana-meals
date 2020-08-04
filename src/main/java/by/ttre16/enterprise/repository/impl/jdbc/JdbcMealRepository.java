package by.ttre16.enterprise.repository.impl.jdbc;

import by.ttre16.enterprise.model.Meal;
import by.ttre16.enterprise.repository.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static by.ttre16.enterprise.util.ProfileUtil.JDBC;
import static org.springframework.dao.support.DataAccessUtils.singleResult;

@Repository
@Profile(JDBC)
public class JdbcMealRepository implements MealRepository {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private static final BeanPropertyRowMapper<Meal> ROW_MAPPER =
            BeanPropertyRowMapper.newInstance(Meal.class);
    private final JdbcUserRepository userRepository;

    @Autowired
    public JdbcMealRepository(JdbcTemplate jdbcTemplate,
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            JdbcUserRepository userRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("meals")
                .usingGeneratedKeyColumns("id");
        this.userRepository = userRepository;
    }

    @Override
    public Collection<Meal> getAll(Integer userId) {
        return jdbcTemplate.query(
                    "SELECT * FROM meals WHERE user_id = ?" +
                    " ORDER BY date_time DESC", ROW_MAPPER, userId);
    }

    @Override
    public Meal save(Integer userId, Meal meal) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", meal.getId())
                .addValue("userId", userId)
                .addValue("calories", meal.getCalories())
                .addValue("dateTime", meal.getDateTime())
                .addValue("description", meal.getDescription());
        if (meal.isNew()) {
            Number key = simpleJdbcInsert.executeAndReturnKey(parameterSource);
            meal.setId(key.intValue());
        } else if (namedParameterJdbcTemplate.update(
                        "UPDATE meals SET calories=:calories, " +
                        "description=:description, " +
                        "date_time=:dateTime " +
                        "WHERE id=:id", parameterSource) == 0) {
            return null;
        }
        return meal;
    }

    @Override
    public boolean deleteOne(Integer userId, Integer mealId) {
        return jdbcTemplate.update("DELETE FROM meals WHERE id = ? " +
                "AND user_id = ?", mealId, userId) != 0;
    }

    @Override
    public Optional<Meal> getOne(Integer userId, Integer mealId) {
        List<Meal> meal = jdbcTemplate.query(
                "SELECT * FROM meals WHERE id = ? AND user_id = ?",
                ROW_MAPPER, mealId, userId);
        return Optional.ofNullable(singleResult(meal));
    }

    @Override
    public boolean deleteAll(Integer userId) {
        return jdbcTemplate.update(
                "DELETE FROM meals where user_id = ?", userId) != 0;
    }

    @Override
    public Collection<Meal> getBetweenHalfOpen(LocalDateTime startDateTime,
            LocalDateTime endDateTime, Integer userId) {
        return jdbcTemplate.query(
                "SELECT * FROM meals WHERE user_id = ? " +
                "AND date_time >= ? AND date_time < ? ORDER BY date_time DESC",
                ROW_MAPPER, userId, startDateTime, endDateTime);
    }

    @Override
    public Optional<Meal> getWithUser(Integer userId, Integer id) {
        return getOne(userId, id).flatMap(meal -> {
            meal.setUser(userRepository.get(userId).orElse(null));
            return Optional.of(meal);
        });
    }
}
