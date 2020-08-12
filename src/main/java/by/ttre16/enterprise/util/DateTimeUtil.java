package by.ttre16.enterprise.util;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static java.util.Objects.*;

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


    public static LocalDateTime atStartOfDayOrMin(LocalDateTime ldt) {
        return nonNull(ldt)
                ? ldt
                : MIN_DATE;
    }

    public static LocalDateTime atEndOfDayOrMax(LocalDateTime ldt) {
        return nonNull(ldt)
                ? ldt
                : MAX_DATE;
    }

    public static LocalDateTime atStartOfDayOrMin(LocalDate ld, LocalTime lt) {
        if (isNull(ld)) {
            return MIN_DATE;
        }
        if (isNull(lt)) {
            return ld.atStartOfDay();
        }
        return LocalDateTime.of(ld, lt);
    }

    public static LocalDateTime atEndOfDayOrMax(LocalDate ld, LocalTime lt) {
        if (isNull(ld)) {
            return MAX_DATE;
        }
        if (isNull(lt)) {
            return ld.atStartOfDay();
        }
        return LocalDateTime.of(ld, lt);
    }

    @Nullable
    public static LocalDate parseLocalDate(@Nullable String str) {
        return StringUtils.isEmpty(str) ? null : LocalDate.parse(str);
    }

    @Nullable
    public static LocalTime parseLocalTime(@Nullable String str) {
        return StringUtils.isEmpty(str) ? null : LocalTime.parse(str);
    }
}
