package by.ttre16.enterprise.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(DatabaseConfiguration.class)
@ComponentScan({
"by.ttre16.enterprise.repository",
        "by.ttre16.enterprise.service",
        "by.ttre16.enterprise.controller"
})
public class ApplicationConfiguration { }
