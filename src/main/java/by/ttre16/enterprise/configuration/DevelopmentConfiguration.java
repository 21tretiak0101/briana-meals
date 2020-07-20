package by.ttre16.enterprise.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import static java.lang.Boolean.parseBoolean;

/**
 *
 * Development specific configuration - creates a localhost postgresql
 * datasource and inserts some test data on the database.
 *
 * Set -Dspring.profiles.active=development to activate this config.
 *
 */
@Configuration
@Profile("development")
@PropertySource({"classpath:development/postgresql.properties",
        "classpath:hibernate.properties"})
public class DevelopmentConfiguration {

    private final Environment environment;

    @Autowired
    public DevelopmentConfiguration(Environment environment) {
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
    public HibernateJpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdapter =
                new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setShowSql(parseBoolean(
                environment.getProperty("jpa.show_sql")));
        jpaVendorAdapter.setDatabasePlatform(
                environment.getProperty("hibernate.dev.dialect"));
        return jpaVendorAdapter;
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer() {
        ResourceDatabasePopulator resourceDatabasePopulator =
                new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScript(new ClassPathResource(
                environment.getRequiredProperty("init-script.path")));
        DataSourceInitializer dataSourceInitializer =
                new DataSourceInitializer();
        dataSourceInitializer.setDataSource(dataSource());
        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
        return dataSourceInitializer;
    }
}