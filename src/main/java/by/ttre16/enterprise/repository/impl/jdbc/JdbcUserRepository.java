package by.ttre16.enterprise.repository.impl.jdbc;

import by.ttre16.enterprise.model.Role;
import by.ttre16.enterprise.model.User;
import by.ttre16.enterprise.repository.UserRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static by.ttre16.enterprise.util.profile.ProfileUtil.JDBC;
import static java.util.Objects.nonNull;
import static org.slf4j.LoggerFactory.getLogger;

@Repository
@Profile(JDBC)
public class JdbcUserRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private static final BeanPropertyRowMapper<User> ROW_MAPPER =
            BeanPropertyRowMapper.newInstance(User.class);
    private static final Logger log = getLogger(JdbcUserRepository.class);
    private final JdbcMealRepository mealRepository;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate,
      NamedParameterJdbcTemplate namedParameterJdbcTemplate,
      @Lazy JdbcMealRepository mealRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        this.mealRepository = mealRepository;
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return jdbcTemplate.query(
                "SELECT * FROM users" +
                        " WHERE email = ?", ROW_MAPPER, email).stream()
                .peek(this::setRoles)
                .findFirst();
    }

    @Override
    public Collection<User> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM users ORDER BY name, email", ROW_MAPPER)
                .stream()
                .peek(this::setRoles)
                .collect(Collectors.toList());
    }

    @Override
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource =
                new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number key = simpleJdbcInsert.executeAndReturnKey(parameterSource);
            user.setId(key.intValue());
        } else {
            if (namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, " +
                            "password=:password, registered=:registered, " +
                            "enabled=:enabled, " +
                            "calories_per_day=:caloriesPerDay " +
                            "WHERE id=:id", parameterSource) == 0) {
                return null;
            }
            deleteRoles(user);
        }
        insertRoles(user);
        return user;
    }

    @Override
    public Optional<User> get(Integer id) {
        return jdbcTemplate.query(
                "SELECT * FROM users WHERE id = ?", ROW_MAPPER, id).stream()
                .peek(this::setRoles)
                .findFirst();
    }

    @Override
    public boolean deleteById(Integer id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id = ?", id) != 0;
    }

    private void setRoles(User user) {
        if (nonNull(user)) {
            List<Integer> roleIds = jdbcTemplate.queryForList(
                    "SELECT ur.role_id FROM user_roles ur " +
                    "WHERE ur.user_id = ?", Integer.class, user.getId());
            log.info("Role ids: {}", roleIds);
            List<Role> roles = new ArrayList<>();
            roleIds.forEach(roleId -> {
                Role role = jdbcTemplate
                        .query("SELECT * FROM roles where id = ?",
                                BeanPropertyRowMapper.newInstance(Role.class),
                                roleId).iterator().next();
                log.info("Found a role: {}", role);
                roles.add(role);
            } );
            user.setRoles(roles);
        }
    }

    @Override
    public Optional<User> getWithMeals(Integer id) {
        return get(id).flatMap(user -> {
            user.setMeals(new ArrayList<>(mealRepository.getAll(id)));
            return Optional.of(user);
        });
    }

    private void insertRoles(User user) {
        List<Role> roles = user.getRoles();
        if (!CollectionUtils.isEmpty(roles)) {
            jdbcTemplate.batchUpdate(
                    "INSERT INTO user_roles (user_id, role_id) " +
                    "VALUES (?, ?)", roles, roles.size(),
                    (ps, role) -> {
                        ps.setInt(1, user.getId());
                        ps.setInt(2, role.getId());
                    });
        }
    }

    private void deleteRoles(User user) {
        jdbcTemplate.update(
                "DELETE FROM user_roles WHERE user_id=?", user.getId());
    }
}
