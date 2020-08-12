package by.ttre16.enterprise.configuration.root;

import by.ttre16.enterprise.annotation.ProfileProperties;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static by.ttre16.enterprise.util.profile.ProfilePropertiesUtil.*;
import static by.ttre16.enterprise.util.profile.ProfileUtil.*;
import static java.lang.Boolean.parseBoolean;
import static org.hibernate.cfg.AvailableSettings.*;

@Configuration
@PropertySource({"classpath:hibernate.properties"})
@EnableTransactionManagement
public class DatabaseConfiguration {

    private final Environment environment;
    private final DataSource dataSource;
    private final Properties properties;

    @Autowired
    public DatabaseConfiguration(Environment environment,
            DataSource dataSource, @ProfileProperties Properties properties) {
        this.environment = environment;
        this.dataSource = dataSource;
        this.properties = properties;
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
       entityManagerFactoryBean.setJpaProperties(properties);
       entityManagerFactoryBean.setDataSource(dataSource);
       entityManagerFactoryBean.setPackagesToScan("by.ttre16.**.model");
       entityManagerFactoryBean.setJpaPropertyMap(jpaPropertyMap());
       return entityManagerFactoryBean;
    }

    @Bean
    @Profile(NOT_JDBC)
    public PlatformTransactionManager transactionManager(
            EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setDataSource(dataSource);
        tm.setEntityManagerFactory(entityManagerFactory);
        return tm;
    }

    @Bean
    public HibernateJpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdapter =
                new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setShowSql(parseBoolean(
                environment.getProperty("jpa.show_sql")));
        jpaVendorAdapter.setDatabasePlatform(
                properties.getProperty(HIBERNATE_DIALECT));
        return jpaVendorAdapter;
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer() {
        ResourceDatabasePopulator resourceDatabasePopulator =
                new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScript(new ClassPathResource(
                properties.getProperty(INIT_SCRIPT_LOCATION)));

        String populateScript = properties.getProperty(
                POPULATE_SCRIPT_LOCATION);
        if (populateScript != null) {
            resourceDatabasePopulator.addScript(
                    new ClassPathResource(populateScript));
        }

        DataSourceInitializer dataSourceInitializer =
                new DataSourceInitializer();
        dataSourceInitializer.setEnabled(
                parseBoolean(properties.getProperty(DATABASE_ENABLED)));
        dataSourceInitializer.setDataSource(dataSource);
        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
        return dataSourceInitializer;
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
        jpaPropertyMap.put(CACHE_REGION_FACTORY,
                environment.getProperty("hibernate.cache_region_factory"));
        jpaPropertyMap.put(ConfigSettings.PROVIDER,
                environment.getProperty("hibernate.cache_provider"));
        jpaPropertyMap.put(USE_SECOND_LEVEL_CACHE,
                environment.getProperty("hibernate.use_second_level_cache"));
        jpaPropertyMap.put(USE_QUERY_CACHE,
                environment.getProperty("hibernate.use_query_cache"));
        String missingCacheStrategyKey =
                "hibernate.javax.cache.missing_cache_strategy";
        jpaPropertyMap.put(missingCacheStrategyKey,
                environment.getProperty(missingCacheStrategyKey));
        return jpaPropertyMap;
    }

    @Bean
    @Profile(JDBC)
    public DataSourceTransactionManager dataSourceTransactionManager() {
        DataSourceTransactionManager transactionManager =
                new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return  transactionManager;
    }
}
