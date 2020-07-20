package by.ttre16.enterprise.service.util;

import by.ttre16.enterprise.util.exception.NotFoundException;
import org.junit.function.ThrowingRunnable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

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

    public static void assertThrowsNotFoundException(ThrowingRunnable tr) {
        assertThrows(NotFoundException.class, tr);
    }
}
