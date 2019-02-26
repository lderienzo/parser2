/*
 * Created by Luke DeRienzo on 2/25/19 9:34 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 2/24/19 12:50 PM
 */

package com.ef.constants;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestConstants {
    public static final int THRESHOLD_TO_FIND_MULTIPLE_IPS_FOR_DAY = 9;
    public static final int THRESHOLD_200 = 200;
    public static final int THRESHOLD_TO_FIND_ONE_IP_FOR_DAY = 27;
    public static final int THRESHOLD_TO_FIND_ONE_IP_FOR_HOUR = 29;
    public static final int THRESHOLD_TO_FIND_MULTIPLE_IPS_FOR_HOUR = 5;
    public static final String THRESHOLD_FOR_FINDING_SIX_IPS_IN_HOUR = "5";
    public static final int NUM_OF_TEST_ACCESS_LOG_FILE_ENTRIES = 1835;
    public static final String INVALID_LINE_FORMAT_LOG_FILE = "invalid_line_format.log";
    public static final String TEST_ACCESS_LOG_FILE = "test_access.log";
    public static final String BOGUS_TEST_LOG_FILE_PATH = "/bogus/path/to/hourly_test_access.log";
    public static final String VALID_TEST_LOG_FILE_PATH = "/Users/lderienzo/IdeaProjects/parser2/src/test/resources/test_access.log";
    public static final String HOURLY_TEST_START_DATE = "2017-01-01.15:00:00";
    public static final String HOURLY_TEST_START_DATE_AFTER_CONVERSION_TO_LCL_DT_TIME = "2017-01-01T15:00";
    public static final String HOURLY_TEST_IP = "192.168.11.231";
    public static final long HOURLY_TEST_IP_LONG = 3232238567L;
    public static final String TEST_START_DATE ="2017-01-01.00:00:00";
    public static final String ASSERT_EQUALS_MSG = "Actual does not equal expected.";
    public static final String INVALID_VALUE = "Invalid value";
    public static final String IP_MATCHING_REGEX= "^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
    public static final List<Long> EXPECTED_MULTIPLE_BLOCKED_IPS_FOR_HOUR =
            Stream.of(3232245325L, 3232248781L, 3232272305L,
                    3232278978L, 3232287599L, 3232295506L).collect(
                    Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
}
