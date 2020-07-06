package by.ttre16.enterprise.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Boolean.parseBoolean;
import static org.hibernate.cfg.AvailableSettings.*;

@Configuration
@PropertySource({"classpath:database.properties",
        "classpath:hibernate.properties"})
@EnableTransactionManagement
public class DatabaseConfiguration {

    private final Environment environment;

    @Autowired
    public DatabaseConfiguration(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(
                environment.getRequiredProperty("database.driver"));
        dataSource.setUrl(environment.getProperty("database.url"));
        dataSource.setUsername(environment.getProperty("database.username"));
        dataSource.setPassword(environment.getProperty("database.password"));
        return dataSource;
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer() {
        ResourceDatabasePopulator resourceDatabasePopulator =
                new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScript(new ClassPathResource("init.sql"));
        DataSourceInitializer dataSourceInitializer =
                new DataSourceInitializer();
        dataSourceInitializer.setDataSource(dataSource());
        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
        return dataSourceInitializer;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(jdbcTemplate());
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
       LocalContainerEntityManagerFactoryBean entityManagerFactoryBean =
               new LocalContainerEntityManagerFactoryBean();
       entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter());
       entityManagerFactoryBean.setDataSource(dataSource());
       entityManagerFactoryBean.setJpaPropertyMap(jpaPropertyMap());
       entityManagerFactoryBean.setPackagesToScan("by.ttre16.**.model");
       return entityManagerFactoryBean;
    }

    @Bean
    public HibernateJpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdapter =
                new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setShowSql(parseBoolean(
                environment.getProperty("jpa.show_sql")));
        jpaVendorAdapter.setDatabasePlatform(
                environment.getProperty("hibernate.dialect"));
        return jpaVendorAdapter;
    }

    @Bean
    public Map<String, String> jpaPropertyMap() {
        Map<String, String> jpaPropertyMap = new HashMap<>();
        jpaPropertyMap.put(FORMAT_SQL,
                environment.getProperty("hibernate.format_sql"));
        jpaPropertyMap.put(USE_SQL_COMMENTS,
                environment.getProperty("hibernate.use_sql_comments"));
        jpaPropertyMap.put(HBM2DDL_AUTO,
                environment.getProperty("hibernate.hbm2ddl.auto"));
        return jpaPropertyMap;
    }

    @Bean
    public PlatformTransactionManager transactionManager(
            EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setDataSource(dataSource());
        tm.setEntityManagerFactory(entityManagerFactory);
        return tm;
    }
}
