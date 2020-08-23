package by.ttre16.enterprise.configuration.root;

import by.ttre16.enterprise.configuration.root.security.SecurityConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan({
"by.ttre16.enterprise.repository",
        "by.ttre16.enterprise.service",
        "by.ttre16.enterprise.configuration.root"
})
@EnableJpaRepositories(basePackages = "by.ttre16.enterprise.repository")
@Import(SecurityConfiguration.class)
public class RootContextConfiguration { }
