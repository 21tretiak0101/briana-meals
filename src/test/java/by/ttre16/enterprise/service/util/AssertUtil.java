package by.ttre16.enterprise.service.util;

import by.ttre16.enterprise.exception.NotFoundException;
import org.junit.function.ThrowingRunnable;
import org.slf4j.Logger;
import org.assertj.core.api.recursive.comparison
        .RecursiveComparisonConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.slf4j.LoggerFactory.getLogger;

public class AssertUtil {
    private static final Logger log = getLogger("result");

    public static <T> void assertMatch(T expected, T actual,
            String ...ignoring) {
        RecursiveComparisonConfiguration recursiveComparisonConfiguration =
                new RecursiveComparisonConfiguration();
        recursiveComparisonConfiguration.ignoreCollectionOrder(true);
        recursiveComparisonConfiguration.ignoreFields(ignoring);
        assertThat(actual)
                .usingRecursiveComparison(recursiveComparisonConfiguration)
                .isEqualTo(expected);
    }

    public static void assertThrowsNotFoundException(ThrowingRunnable tr) {
        assertThrows(NotFoundException.class, tr);
    }

    public static <T extends Throwable> void validateRootCause(
            Runnable runnable, Class<T> rootExceptionClass) {
        assertThrows(rootExceptionClass, () -> {
            try {
                runnable.run();
            } catch (Exception e) {
                throw getRootCause(e);
            }
        });
    }

    public static Throwable getRootCause(Throwable throwable) {
        Throwable result = throwable;
        Throwable cause;
        while ((cause = result.getCause()) != null && (result != cause)) {
            result = cause;
        }
        log.warn("Exception root cause: ", cause);
        return result;
    }
}
