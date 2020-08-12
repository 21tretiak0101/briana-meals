package by.ttre16.enterprise.controller.util.matcher;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import static by.ttre16.enterprise.controller.util.json.JsonUtil.readValue;
import static by.ttre16.enterprise.controller.util.json.JsonUtil.readValues;
import static by.ttre16.enterprise.service.util.AssertUtil.assertMatch;

import java.io.UnsupportedEncodingException;

public class TestMatcher<T> {
    private final Class<T> clazz;
    private final String[] ignoringFields;

    public TestMatcher(Class<T> clazz, String ... ignoringFields) {
        this.clazz = clazz;
        this.ignoringFields = ignoringFields;
    }

    public ResultMatcher contentJson(T expected) {
        return result -> assertMatch(expected,
                readValue(getContent(result), clazz), ignoringFields);
    }

    public ResultMatcher contentJson(Iterable<T> expected) {
        return result -> assertMatch(expected,
                readValues(getContent(result), clazz), ignoringFields);
    }

    public static String getContent(MvcResult result)
            throws UnsupportedEncodingException {
        return result.getResponse().getContentAsString();
    }
}
