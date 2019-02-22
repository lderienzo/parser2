/*
 * Created by Luke DeRienzo on 8/15/18 12:29 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 6/22/18 11:32 PM
 */

package com.ef.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// TODO: clean up
public class ParserUtils {

    private static final String ARGUMENT_DATE_FORMAT = "yyyy-MM-dd.HH:mm:ss";
    private static final String LOG_FILE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    public static final String NO_BLOCKED_IPS_TO_REPORT = "No blocked IPs present to report.";
    public static final String NO_BLOCKED_IPS_TO_SAVE = "No blocked IPs present to save.";
    public static final String BLOCKED_IPS_MESSAGE_HEADER = "Blocked IPs:";
    public static final DateTimeFormatter LOG_FILE_DATE_FORMATTER = DateTimeFormatter.ofPattern(LOG_FILE_DATE_FORMAT);
    public static final DateTimeFormatter ARGUMENT_DATE_FORMATTER = DateTimeFormatter.ofPattern(ARGUMENT_DATE_FORMAT);
    public static final int THRESHOLD_100 = 100;


    public static LocalDateTime stringToLocalDateTime(String date) {
        return LocalDateTime.parse(date, ARGUMENT_DATE_FORMATTER);
    }

    public static int stringToInt(String value) {
        return Integer.parseInt(value);
    }
}
