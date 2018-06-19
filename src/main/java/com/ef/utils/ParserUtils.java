package com.ef.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ParserUtils {

    public static final String ARGUMENT_DATE_FORMAT = "yyyy-MM-dd.HH:mm:ss";
    public static final String LOG_FILE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DB_PWD = "password";
    public static final String NO_BLOCKED_IPS_TO_REPORT = "No blocked IPs present to report.";
    public static final String NO_BLOCKED_IPS_TO_SAVE = "No blocked IPs present to save.";
    public static final String BLOCKED_IPS_MESSAGE_HEADER = "Blocked IPs:";
    public static final String STATUS_MESSAGE_SUCCESS = "Operation Successful";
    public static final String STATUS_MESSAGE_FAILURE = "Operation Unsuccessful";
    public static final DateTimeFormatter LOG_FILE_DATE_FORMATTER = DateTimeFormatter.ofPattern(LOG_FILE_DATE_FORMAT);
    public static final DateTimeFormatter ARGUMENT_DATE_FORMATTER = DateTimeFormatter.ofPattern(ARGUMENT_DATE_FORMAT);
    public static final int THRESHOLD_100 = 100;


    public static LocalDateTime stringToLocalDateTime(String date) {
        return LocalDateTime.parse(date, ARGUMENT_DATE_FORMATTER);
    }
}
