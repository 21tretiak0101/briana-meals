package by.ttre16.enterprise.util;

import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static final LocalDateTime MIN_DATE = LocalDateTime.of(
            1, 1, 1, 0, 0);

    private static final LocalDateTime MAX_DATE = LocalDateTime.of(
            3000, 1, 1, 0, 0);

    public static boolean isBetweenHalfOpen(LocalTime time,
            LocalTime startTime, LocalTime endTime) {
        return time.compareTo(startTime) >= 0 && time.compareTo(endTime) < 0;
    }

    public static String toString(LocalDateTime dateTime) {
        return isNull(dateTime) ? "" : dateTime.format(DATE_TIME_FORMATTER);
    }

    public static <T extends Comparable<T>> boolean isBetweenHalfOpen(T value,
            @Nullable T start, @Nullable T end) {
        return (isNull(start) || value.compareTo(start) >= 0)
                && (isNull(end) || value.compareTo(end) < 0);
    }


    public static LocalDateTime atStartOfDayOrMin(LocalDate ldt) {
        return nonNull(ldt)
                ? ldt.atStartOfDay()
                : MIN_DATE;
    }

    public static LocalDateTime atStartOfNextDayOrMax(LocalDate ldt) {
        return nonNull(ldt)
                ? ldt.plus(1, ChronoUnit.DAYS).atStartOfDay()
                : MAX_DATE;
    }
}
