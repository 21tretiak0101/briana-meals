package by.ttre16.enterprise.configuration.root;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan({
"by.ttre16.enterprise.repository",
        "by.ttre16.enterprise.service",
        "by.ttre16.enterprise.configuration.root"
})
@EnableJpaRepositories(basePackages = "by.ttre16.enterprise.repository")
public class RootContextConfiguration { }
