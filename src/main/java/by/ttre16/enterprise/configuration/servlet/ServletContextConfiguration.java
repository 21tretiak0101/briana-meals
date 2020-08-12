package by.ttre16.enterprise.configuration.servlet;

import by.ttre16.enterprise.formatter.LocalDateFormatter;
import by.ttre16.enterprise.formatter.LocalTimeFormatter;
import by.ttre16.enterprise.security.SecurityUtil;
import by.ttre16.enterprise.service.UserService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json
        .MappingJackson2HttpMessageConverter;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.util.List;

import static com.fasterxml.jackson.annotation.PropertyAccessor.ALL;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;
import static com.fasterxml.jackson.databind.SerializationFeature
        .WRITE_DATES_AS_TIMESTAMPS;

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
    }

    @Bean()
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

    @Override
    public void configureMessageConverters(
            List<HttpMessageConverter<?>> converters) {
        converters.add(messageConverter());
    }

    @Bean
    public MappingJackson2HttpMessageConverter messageConverter() {
        MappingJackson2HttpMessageConverter converter =
                new MappingJackson2HttpMessageConverter();
        converter.getObjectMapper()
                .registerModules(new Hibernate5Module(), new JavaTimeModule())
                .configure(WRITE_DATES_AS_TIMESTAMPS, false)
                .setVisibility(ALL, JsonAutoDetect.Visibility.NONE)
                .setVisibility(FIELD, JsonAutoDetect.Visibility.ANY)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return converter;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new LocalTimeFormatter());
        registry.addFormatter(new LocalDateFormatter());
    }
}
