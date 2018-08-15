/*
 * Created by Luke DeRienzo on 8/15/18 12:29 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 8/15/18 11:50 AM
 */

package com.ef.utils;


public class ParserTestUtils extends ParserUtils {
    public static final int THRESHOLD_200 = 200;
    public static final int THRESHOLD_500 = 500;
    public static final String INVALID_LINE_FORMAT_LOG_FILE = "invalid_line_format.log";
    public static final String SHORT_TEST_ACCESS_LOG_FILE = "short_test_access.log";
    public static final String BOGUS_TEST_LOG_FILE_PATH = "/bogus/path/to/hourly_test_access.log";
    public static final String VALID_TEST_LOG_FILE_PATH = "/Users/lderienzo/IdeaProjects/parser2/src/test/resources/hourly_test_access.log";

    public static final String HOURLY_TEST_START_DATE = "2017-01-01.15:00:00";
    public static final String HOURLY_TEST_ACCESS_LOG = "hourly_test_access.log";
    public static final String HOURLY_TEST_IP = "192.168.11.231";
    public static final long HOURLY_TEST_IP_LONG = 3232238567L;

    public static final String DAILY_TEST_START_DATE ="2017-01-01.00:00:00";
    public static final String DAILY_TEST_ACCESS_LOG = "daily_test_access.log";
    public static final String DAILY_TEST_IP = "192.168.102.136";
    public static final long DAILY_TEST_IP_LONG = 3232261768L;
}
