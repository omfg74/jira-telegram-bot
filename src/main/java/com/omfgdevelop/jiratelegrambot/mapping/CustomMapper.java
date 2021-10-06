package com.omfgdevelop.jiratelegrambot.mapping;

import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class CustomMapper {
    private static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final SimpleDateFormat ISO_DATE_TIME = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    public static String toString(LocalDate value) {
        return value == null ? null : value.format(ISO_DATE);
    }

    public static LocalDate stringToLocalDate(String value) {
        return StringUtils.isBlank(value) ? null : LocalDate.parse(value, DateTimeFormatter.ISO_DATE);
    }

    public static LocalDate tryParseLocalDate(String value) {
        if (StringUtils.isBlank(value)) return null;

        boolean valueSpecified = false;
        LocalDate parsed = null;

        try {
            parsed = LocalDate.parse(value);
            valueSpecified = true;
        } catch (DateTimeParseException ignored) {
        }

        return valueSpecified ? parsed : null;
    }

    public static LocalDateTime tryParseLocalDateTime(String value) {
        if (StringUtils.isBlank(value)) return null;

        boolean valueSpecified = false;
        LocalDateTime parsed = null;

        try {
            parsed = LocalDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME);
            valueSpecified = true;
        } catch (DateTimeParseException ignored) {
        }

        return valueSpecified ? parsed : null;
    }
}
