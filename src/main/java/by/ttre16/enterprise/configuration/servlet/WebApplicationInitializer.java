package by.ttre16.enterprise.configuration.servlet;

import by.ttre16.enterprise.configuration.root.RootContextConfiguration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.springframework.web.servlet.support.
        AbstractAnnotationConfigDispatcherServletInitializer;

import static by.ttre16.enterprise.util.ProfileUtil.DATA_JPA;
import static by.ttre16.enterprise.util.ProfileUtil.DEVELOPMENT;

public class WebApplicationInitializer
        extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    public void onStartup(ServletContext context) throws ServletException {
        super.onStartup(context);
        context.setInitParameter("spring.profiles.active",
                DEVELOPMENT + ", " + DATA_JPA);
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{RootContextConfiguration.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{ServletContextConfiguration.class};
    }
}
