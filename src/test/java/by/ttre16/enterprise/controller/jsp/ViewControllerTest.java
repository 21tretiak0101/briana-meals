package by.ttre16.enterprise.controller.jsp;

import org.junit.Test;

import static org.springframework.test.web.servlet.request
        .MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result
        .MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.
        MockMvcResultMatchers.*;

public class ViewControllerTest extends AbstractJspControllerTest {
    @Test
    public void indexViewController() throws Exception {
        perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/index.jsp"))
                .andDo(print());
    }
}
