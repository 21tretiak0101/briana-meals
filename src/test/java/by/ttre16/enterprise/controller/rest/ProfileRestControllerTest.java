package by.ttre16.enterprise.controller.rest;

import by.ttre16.enterprise.data.UserInfoTestData;
import by.ttre16.enterprise.model.User;
import by.ttre16.enterprise.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static by.ttre16.enterprise.controller.util.json.JsonUtil.writeValue;
import static by.ttre16.enterprise.data.UserInfoTestData.*;
import static by.ttre16.enterprise.data.UserTestData.*;
import static by.ttre16.enterprise.util.web.UrlUtil.PROFILE_REST_URL;
import static java.util.Collections.singletonList;
import static org.springframework.test.web.servlet.request
        .MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result
        .MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.
        MockMvcResultMatchers.*;

public class ProfileRestControllerTest extends AbstractRestControllerTest {

    @Autowired
    private UserService userService;

    @Override
    public Integer getTestUserId() {
        return USER_ID;
    }

    @Test
    public void getOne() throws Exception {
        perform(get(PROFILE_REST_URL)
                .header(authorizationHeader, bearerToken))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_INFO_TEST_MATCHER
                        .contentJson(
                                map(USERS.get(getTestUserId()))
                        )
                )
                .andDo(print());
    }

    @Test
    public void update() throws Exception {
        User updated = getUpdated(getTestUserId());
        perform(put(PROFILE_REST_URL)
                .header(authorizationHeader, bearerToken)
                .contentType(MediaType.APPLICATION_JSON)
                    .content(writeValue(UserInfoTestData.map(updated))))
                .andExpect(status().isNoContent())
                .andDo(print());
        assertMatch(updated, userService.get(getTestUserId()));
    }

    @Test
    public void deleteOne() throws Exception {
        perform(delete(PROFILE_REST_URL)
                .header(authorizationHeader, bearerToken))
                .andExpect(status().isNoContent())
                .andDo(print());
        assertMatch(
                singletonList(USERS.get(ADMIN_ID)),
                userService.getAll()
        );
    }
}
