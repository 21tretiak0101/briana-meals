package by.ttre16.enterprise.service.util;

import static org.assertj.core.api.Assertions.assertThat;

public class AssertUtil {
    public static <T> void assertMatch(Iterable<T> expected,
            Iterable<T> actual, String ...ignoring) {
        assertThat(actual)
                .usingElementComparatorIgnoringFields(ignoring)
                .containsExactlyInAnyOrderElementsOf(expected);
    }

    public static <T> void assertMatch(T expected, T actual,
            String ...ignoring) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, ignoring);
    }
}
