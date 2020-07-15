package by.ttre16.enterprise.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static org.hibernate.cfg.AvailableSettings.*;

@Configuration
@PropertySource({"classpath:development/postgresql.properties",
        "classpath:hibernate.properties"})
@EnableTransactionManagement
@Import({DevelopmentConfiguration.class, TestConfiguration.class})
public class DatabaseConfiguration {

    private final Environment environment;
    private final DataSource dataSource;

    @Autowired
    public DatabaseConfiguration(Environment environment,
            DataSource dataSource) {
        this.environment = environment;
        this.dataSource = dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(jdbcTemplate());
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            JpaVendorAdapter jpaVendorAdapter) {
       LocalContainerEntityManagerFactoryBean entityManagerFactoryBean =
               new LocalContainerEntityManagerFactoryBean();
       entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
       entityManagerFactoryBean.setDataSource(dataSource);
       entityManagerFactoryBean.setPackagesToScan("by.ttre16.**.model");

        Map<String, String> jpaPropertyMap = new HashMap<>();
        jpaPropertyMap.put(FORMAT_SQL,
                environment.getProperty("hibernate.format_sql"));
        jpaPropertyMap.put(USE_SQL_COMMENTS,
                environment.getProperty("hibernate.use_sql_comments"));
        jpaPropertyMap.put(HBM2DDL_AUTO,
                environment.getProperty("hibernate.hbm2ddl.auto"));
       entityManagerFactoryBean.setJpaPropertyMap(jpaPropertyMap);

       return entityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(
            EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setDataSource(dataSource);
        tm.setEntityManagerFactory(entityManagerFactory);
        return tm;
    }
}
