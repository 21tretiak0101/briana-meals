package by.ttre16.enterprise.controller;

import by.ttre16.enterprise.AbstractTest;
import by.ttre16.enterprise.configuration.root.RootContextConfiguration;
import by.ttre16.enterprise.configuration.root.security.SecurityConfiguration;
import by.ttre16.enterprise.configuration.servlet.ServletContextConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.converter.json
        .MappingJackson2HttpMessageConverter;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.MockMvcConfigurer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.request
        .MockHttpServletRequestBuilder;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.annotation.PostConstruct;

import static by.ttre16.enterprise.util.profile.ProfileUtil.DATA_JPA;
import static by.ttre16.enterprise.util.profile.ProfileUtil.TEST;
import static java.util.Objects.requireNonNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ContextConfiguration(classes = {
        ServletContextConfiguration.class,
        RootContextConfiguration.class
})
@WebAppConfiguration
@ActiveProfiles({TEST, DATA_JPA})
public abstract class AbstractControllerTest extends AbstractTest {
    private static final CharacterEncodingFilter CHARACTER_ENCODING_FILTER =
            new CharacterEncodingFilter();

    public static ObjectMapper objectMapper;

    static {
        CHARACTER_ENCODING_FILTER.setEncoding("UTF-8");
        CHARACTER_ENCODING_FILTER.setForceEncoding(true);
    }

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    protected MappingJackson2HttpMessageConverter messageConverter;

    @Autowired
    private CacheManager cacheManager;

    @Before
    public void setUp() {
        requireNonNull(cacheManager.getCache("users")).clear();
    }

    @PostConstruct
    private void init() {
        objectMapper = messageConverter.getObjectMapper();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilter(CHARACTER_ENCODING_FILTER)
                .apply(springSecurity())
                .build();
    }

    protected ResultActions perform(
            MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder);
    }
}
