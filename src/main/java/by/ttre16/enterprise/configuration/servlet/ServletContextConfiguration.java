package by.ttre16.enterprise.configuration.servlet;

import by.ttre16.enterprise.security.SecurityUtil;
import by.ttre16.enterprise.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@EnableWebMvc
@ComponentScan({"by.ttre16.enterprise.controller"})
@PropertySource("classpath:app.properties")
public class ServletContextConfiguration implements WebMvcConfigurer {
    private final Environment environment;

    @Autowired
    public ServletContextConfiguration(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public ViewResolver internalResourceViewResolver() {
        InternalResourceViewResolver viewResolver =
                new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/meals/edit");
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource =
                new ResourceBundleMessageSource();
        messageSource.setBasename(
               environment.getRequiredProperty("messages.location"));
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setDefaultEncoding("utf-8");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    @Bean
    @SessionScope
    public SecurityUtil securityUtil(UserService service) {
        return new SecurityUtil(service);
    }
}
