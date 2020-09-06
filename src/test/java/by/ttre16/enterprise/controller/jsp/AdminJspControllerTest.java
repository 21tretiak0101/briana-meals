package by.ttre16.enterprise.controller.jsp;

import org.assertj.core.matcher.AssertionMatcher;
import org.junit.Ignore;
import org.junit.Test;
import by.ttre16.enterprise.model.User;

import java.util.List;

import static by.ttre16.enterprise.data.UserTestData.*;
import static by.ttre16.enterprise.util.web.UrlUtil.ADMIN_JSP_URL;
import static org.springframework.test.web.servlet.request
        .MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result
        .MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.
        MockMvcResultMatchers.*;

@Ignore
public class AdminJspControllerTest extends AbstractJspControllerTest {
    @Test
    public void getAll() throws Exception {
        perform(get(ADMIN_JSP_URL + "/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users.jsp"))
                .andDo(print())
                .andExpect(model().attribute("users",
                        new AssertionMatcher<List<User>>() {
                            @Override
                            public void assertion(List<User> actual)
                                    throws AssertionError {
                                assertMatch(USERS.values(), actual);
                            }
                        }));
    }
}
