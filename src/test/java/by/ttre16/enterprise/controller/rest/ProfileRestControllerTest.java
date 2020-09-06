package by.ttre16.enterprise.controller.rest;

import by.ttre16.enterprise.data.UserToTestData;
import by.ttre16.enterprise.dto.mapper.UserEntityMapper;
import by.ttre16.enterprise.dto.to.UserTo;
import by.ttre16.enterprise.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static by.ttre16.enterprise.controller.util.json.JsonUtil.readFromJson;
import static by.ttre16.enterprise.controller.util.json.JsonUtil.writeValue;
import static by.ttre16.enterprise.data.UserTestData.*;
import static by.ttre16.enterprise.data.UserToTestData.USER_TO_TEST_MATCHER;
import static by.ttre16.enterprise.data.UserToTestData.getNewUserTo;
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

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private UserEntityMapper userMapper;

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
                .andExpect(USER_TO_TEST_MATCHER
                        .contentJson(
                                userMapper.toDto(USERS.get(getTestUserId()))
                        )
                )
                .andDo(print());
    }

    @Test
    public void update() throws Exception {
        UserTo updated = userMapper.toDto(getUpdated(getTestUserId()));
        perform(put(PROFILE_REST_URL)
                .header(authorizationHeader, bearerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated))
        )
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(
                getUpdated(getTestUserId()),
                userService.get(getTestUserId())
        );
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

    @Test
    public void createProfile() throws Exception {
        MvcResult response = perform(post(PROFILE_REST_URL)
                .header(authorizationHeader, bearerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(getNewUserTo())
                )
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        UserTo createdUserTo = readFromJson(response, UserTo.class);
        UserTo newUserTo = userMapper.toDto(getNewUser());
        newUserTo.setId(createdUserTo.getId());
        UserToTestData.assertMatch(newUserTo, createdUserTo);
    }
}
