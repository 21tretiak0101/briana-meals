package by.ttre16.enterprise.configuration.root;

import by.ttre16.enterprise.annotation.ProfileProperties;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.Properties;

import static by.ttre16.enterprise.util.ProfilePropertiesUtil.*;
import static by.ttre16.enterprise.util.ProfileUtil.DEVELOPMENT;

@Profile(DEVELOPMENT)
@Configuration
@PropertySource({"classpath:development/postgresql.properties"})
public class DevelopmentConfiguration {

    private final Environment environment;

    @Autowired
    public DevelopmentConfiguration(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public DataSource dataSource() {
        DataSource dataSource = new DataSource();
        dataSource.setDriverClassName(
                environment.getRequiredProperty("database.driver"));
        dataSource.setUrl(environment.getProperty("database.url"));
        dataSource.setUsername(environment.getProperty("database.username"));
        dataSource.setPassword(environment.getProperty("database.password"));
        return dataSource;
    }

    @Bean
    @ProfileProperties
    public Properties additionalDevelopmentProperties() {
        Properties properties = new Properties();
        properties.setProperty(DATABASE_ENABLED,
                environment.getProperty("database.init"));
        properties.setProperty(INIT_SCRIPT_LOCATION,
                environment.getProperty("init-script.path"));
        properties.setProperty(HIBERNATE_DIALECT,
                environment.getProperty("database.platform"));
        properties.setProperty(POPULATE_SCRIPT_LOCATION,
                environment.getProperty("populate-script.path"));
        return properties;
    }
}
