package com.ef.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ParserUtils {

    public static final String HOURLY_DURATION = "hourly";
    public static final String DAILY_DURATION = "daily";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DB_PWD = "password";
    public static final String LOG_FILE_NAME = "accesslog";
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

    public static LocalDateTime stringToLocalDateTime(String date) {
        return LocalDateTime.parse(date, formatter);
    }
}
