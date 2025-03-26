package org.noahsark.util;

import org.apache.commons.lang3.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * 日期工具类
 *
 * @author zhangxt
 * @date 2023/09/20 10:08
 **/
public class DateTimeUtils {

    private static final String OFFSET_ID = "+08:00";
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public static OffsetDateTime now() {
        return OffsetDateTime.now();
    }

    public static OffsetDateTime parse(CharSequence text) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return parse(text, formatter);
    }

    public static LocalDate parseDate(CharSequence text) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return parseDate(text, formatter);
    }

    public static LocalDate parseDate(CharSequence text, DateTimeFormatter formatter) {
        if (StringUtils.isEmpty(text) || formatter == null) {
            return null;
        }
        LocalDate localDateTime = LocalDate.parse(text, formatter);

        return localDateTime;
    }

    public static OffsetDateTime parse(CharSequence text, DateTimeFormatter formatter) {
        if (StringUtils.isEmpty(text) || formatter == null) {
            return null;
        }
        LocalDateTime localDateTime = LocalDateTime.parse(text, formatter);
        OffsetDateTime dateTime = OffsetDateTime.of(localDateTime, ZoneOffset.of(OFFSET_ID));
        return dateTime;
    }

    public static String format(OffsetDateTime dateTime) {
        return formatDateTime(dateTime, DATE_TIME_FORMAT);
    }

    public static String formatDateTime(OffsetDateTime dateTime, String patten) {
        if (dateTime == null) {
            return null;
        }
        if (StringUtils.isEmpty(patten)) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(patten));
    }

    public static String format(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public static String format(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
    }

    public static LocalDateTime parseLocalDateTime(CharSequence text) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

        return parseLocalDateTime(text, formatter);
    }

    public static LocalDateTime parseLocalDateTime(CharSequence text, DateTimeFormatter formatter) {
        if (StringUtils.isEmpty(text) || formatter == null) {
            return null;
        }
        LocalDateTime localDateTime = LocalDateTime.parse(text, formatter);

        return localDateTime;
    }

    public static LocalDateTime getDateTimeOfTimestamp(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    public static long getTimestampOfDateTime(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return instant.toEpochMilli();
    }


}
