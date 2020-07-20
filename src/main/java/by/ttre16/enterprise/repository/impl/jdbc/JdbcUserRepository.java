package by.ttre16.enterprise.repository.impl.jdbc;

import by.ttre16.enterprise.model.User;
import by.ttre16.enterprise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

import static by.ttre16.enterprise.util.ProfileUtil.JDBC;

@Repository
@Profile(JDBC)
public class JdbcUserRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private static final BeanPropertyRowMapper<User> ROW_MAPPER =
            BeanPropertyRowMapper.newInstance(User.class);

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate,
            NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return jdbcTemplate.query(
                        "SELECT * FROM users" +
                        " WHERE email = ?", ROW_MAPPER, email).stream()
                .findFirst();
    }

    @Override
    public Collection<User> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
    }

    @Override
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource =
                new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number key = simpleJdbcInsert.executeAndReturnKey(parameterSource);
            user.setId(key.intValue());
        } else if (namedParameterJdbcTemplate.update(
                        "UPDATE users SET name=:name, email=:email, " +
                        "password=:password, registered=:registered, " +
                        "enabled=:enabled, " +
                        "calories_per_day=:caloriesPerDay " +
                        "WHERE id=:id", parameterSource) == 0) {
            return null;
        }
        return user;
    }

    @Override
    public Optional<User> get(Integer id) {
        return jdbcTemplate.query(
                "SELECT * FROM users WHERE id = ?", ROW_MAPPER, id).stream()
        .findFirst();
    }

    @Override
    public boolean deleteById(Integer id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id = ?", id) != 0;
    }
}
