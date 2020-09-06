package by.ttre16.enterprise.controller.rest;

import by.ttre16.enterprise.data.UserToTestData;
import by.ttre16.enterprise.dto.mapper.UserEntityMapper;
import by.ttre16.enterprise.dto.to.UserTo;
import by.ttre16.enterprise.model.User;
import by.ttre16.enterprise.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;

import java.util.stream.Collectors;

import static by.ttre16.enterprise.controller.util.json.JsonUtil.readFromJson;
import static by.ttre16.enterprise.controller.util.json.JsonUtil.writeValue;
import static by.ttre16.enterprise.data.TestData.USER_ID;
import static by.ttre16.enterprise.data.UserTestData.*;
import static by.ttre16.enterprise.data.UserToTestData.*;
import static by.ttre16.enterprise.service.util.AssertUtil
        .assertThrowsNotFoundException;
import static org.springframework.http.MediaType.APPLICATION_JSON;
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

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private UserEntityMapper userMapper;

    @Override
    public Integer getTestUserId() {
        return ADMIN_ID;
    }

    @Test
    public void getAll() throws Exception {
        perform(get(ADMIN_REST_URL)
                .header(authorizationHeader, bearerToken))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(USER_TO_TEST_MATCHER
                        .contentJson(USERS.values().stream()
                                .map(user -> userMapper.toDto(user))
                        .collect(Collectors.toList())))
                .andDo(print());
    }

    @Test
    public void deleteOne() throws Exception {
        perform(delete(ADMIN_REST_URL + "/" + USER_ID)
                .header(authorizationHeader, bearerToken))
                .andExpect(status().isNoContent())
                .andDo(print());
        assertThrowsNotFoundException(() -> userService.get(USER_ID));
    }

    @Test
    public void update() throws Exception {
        User updated = getUpdated(USER_ID);
        perform(put(ADMIN_REST_URL + "/" + USER_ID)
                .header(authorizationHeader, bearerToken)
                .contentType(APPLICATION_JSON)
                    .content(writeValue(userMapper.toDto(updated))))
                .andExpect(status().isNoContent())
                .andDo(print());
        assertMatch(updated, userService.get(USER_ID));
    }

    @Test
    public void createAdmin() throws Exception {
        MvcResult response = perform(post(ADMIN_REST_URL)
                .header(authorizationHeader, bearerToken)
                .contentType(APPLICATION_JSON)
                .content(writeValue(getNewAdminTo())))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(APPLICATION_JSON))
                .andDo(print())
                .andReturn();
        UserTo createdAdminInfo = readFromJson(response, UserTo.class);
        UserTo newAdminTo = userMapper.toDto(getNewAdmin());
        newAdminTo.setId(createdAdminInfo.getId());
        UserToTestData.assertMatch(newAdminTo, createdAdminInfo);
    }
}
