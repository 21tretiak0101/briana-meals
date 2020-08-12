package by.ttre16.enterprise.controller.rest;

import by.ttre16.enterprise.model.User;
import by.ttre16.enterprise.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static by.ttre16.enterprise.controller.util.json.JsonUtil.writeValue;
import static by.ttre16.enterprise.data.TestData.USER_ID;
import static by.ttre16.enterprise.data.UserTestData.*;
import static by.ttre16.enterprise.service.util.AssertUtil
        .assertThrowsNotFoundException;
import static org.springframework.test.web.servlet.request
        .MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result
        .MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.
        MockMvcResultMatchers.*;

import static by.ttre16.enterprise.util.web.UrlUtil.ADMIN_REST_URL;

public class AdminRestControllerTest extends AbstractRestControllerTest {

    @Autowired
    private UserService userService;

    @Test
    public void getAll() throws Exception {
        perform(get(ADMIN_REST_URL))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_TEST_MATCHER.contentJson(USERS.values()))
                .andDo(print());
    }

    @Test
    public void getByEmail() throws Exception {
        User user = USERS.get(USER_ID);
        perform(get(ADMIN_REST_URL + "/by?email=" + user.getEmail()))
                .andExpect(content()
                    .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_TEST_MATCHER.contentJson(user))
                .andDo(print());
    }

    @Test
    public void deleteOne() throws Exception {
        perform(delete(ADMIN_REST_URL + "/" + USER_ID))
                .andExpect(status().isNoContent())
                .andDo(print());
        assertThrowsNotFoundException(() -> userService.get(USER_ID));
    }

    @Test
    public void update() throws Exception {
        User updated = getUpdated(USER_ID);
        perform(put(ADMIN_REST_URL + "/" + USER_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(writeValue(updated)))
                .andExpect(status().isNoContent())
                .andDo(print());
        assertMatch(updated, userService.get(USER_ID));
    }

}
