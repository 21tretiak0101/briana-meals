package by.ttre16.enterprise.service.util;

import org.junit.rules.ExternalResource;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static org.slf4j.LoggerFactory.getLogger;

public class TimingRules {
    private static final Logger log = getLogger("result");
    private static final StringBuilder results = new StringBuilder();
    private static String className;
    private static final String DELIMITER =
            "-------------------------------------------";

    public final static ExternalResource SUMMARY = new ExternalResource() {
        @Override
        protected void after() {
            printResults();
        }

        @Override
        protected void before() {
            results.setLength(0);
            className = null;
        }
    };

    public static final Stopwatch STOPWATCH = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            String result = format("\n%-35s %7d",
                    description.getMethodName(), NANOSECONDS.toMillis(nanos));
            results.append(result);
            setClassName(description.getClassName());
            log.info("\nTest '{}: {}' finished.",className,
                    description.getMethodName());
            log.info("{}\n", DELIMITER);
        }
    };


    private static void setClassName(String name) {
        if (className == null) {
            className = name;
        }
    }

    public static void printResults() {
        log.info("\n\n\nTest class: {}", className);
        log.info("\nTest{} Duration, ms \n{}{}\n{}",
                format("%-26s", " "), DELIMITER, results, DELIMITER);
    }
}
