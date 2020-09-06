package by.ttre16.enterprise.controller.rest;

import org.junit.Test;

import static by.ttre16.enterprise.controller.util.json.JsonUtil.writeValue;
import static by.ttre16.enterprise.data.TestData.ADMIN_ID;
import static by.ttre16.enterprise.data.UserToTestData.getUserToWithInvalidPassword;
import static by.ttre16.enterprise.data.UserToTestData.getUserToWithNonUniqueEmail;
import static by.ttre16.enterprise.exception.ErrorType.VALIDATION_ERROR;
import static by.ttre16.enterprise.util.web.UrlUtil.ADMIN_REST_URL;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ExceptionInfoControllerTest extends AbstractRestControllerTest {
    @Override
    public Integer getTestUserId() {
        return ADMIN_ID;
    }

    @Test
    public void createWithInvalidPassword() throws Exception {
        perform(post(ADMIN_REST_URL)
                .header(authorizationHeader, bearerToken)
                .contentType(APPLICATION_JSON)
                .content(writeValue(getUserToWithInvalidPassword()))
        )
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(
                        jsonPath(
                                "$.errorType",
                                is(VALIDATION_ERROR.name())
                        ));
    }

    @Test
    public void createWithNonUniqueEmail() throws Exception {
        perform(post(ADMIN_REST_URL)
                .header(authorizationHeader, bearerToken)
                .contentType(APPLICATION_JSON)
                .content(writeValue(getUserToWithNonUniqueEmail()))
        )
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(
                        jsonPath(
                                "$.errorType",
                                is(VALIDATION_ERROR.name())
                        )
                );
    }
}
