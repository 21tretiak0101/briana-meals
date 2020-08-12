package by.ttre16.enterprise.configuration.root;

import by.ttre16.enterprise.annotation.ProfileProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.Properties;

import static by.ttre16.enterprise.util.profile.ProfilePropertiesUtil.*;
import static by.ttre16.enterprise.util.profile.ProfileUtil.TEST;

@Profile(TEST)
@Configuration
@PropertySource({"classpath:test/hsqldb.properties"})
public class TestConfiguration {

    private final Environment environment;

    @Autowired
    public TestConfiguration(Environment environment) {
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
    @ProfileProperties
    public Properties additionalTestProperties() {
        Properties properties = new Properties();
        properties.setProperty(DATABASE_ENABLED,
                environment.getProperty("database.init"));
        properties.setProperty(INIT_SCRIPT_LOCATION,
                environment.getProperty("init-script.path"));
        properties.setProperty(HIBERNATE_DIALECT,
                environment.getProperty("database.platform"));
        return properties;
    }
}
