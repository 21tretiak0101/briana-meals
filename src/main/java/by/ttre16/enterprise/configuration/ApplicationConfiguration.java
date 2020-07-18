package by.ttre16.enterprise.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan({
"by.ttre16.enterprise.repository",
        "by.ttre16.enterprise.service",
        "by.ttre16.enterprise.controller",
        "by.ttre16.enterprise.configuration"
})
@EnableJpaRepositories(basePackages = "by.ttre16.enterprise.repository")
public class ApplicationConfiguration { }
