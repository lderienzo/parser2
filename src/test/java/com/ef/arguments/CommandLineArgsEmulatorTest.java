/*
 * Created by Luke DeRienzo on 11/6/18 1:00 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 11/5/18 10:44 PM
 */

package com.ef.arguments;

import static com.ef.utils.ParserTestUtils.BOGUS_TEST_LOG_FILE_PATH;
import static com.ef.utils.ParserTestUtils.TEST_START_DATE;

import org.junit.Test;

import static com.ef.enums.Duration.HOURLY;
import static com.ef.utils.ParserTestUtils.THRESHOLD_200;
import static org.junit.Assert.assertArrayEquals;


public final class CommandLineArgsEmulatorTest {
    private String[] actualArgs;
    private static final String[] EXPECTED_ARGS = {
        "--accesslog=/bogus/path/to/hourly_test_access.log",
        "--startDate=2017-01-01.00:00:00",
        "--duration=hourly",
        "--threshold=200"
    };

    private static final String[] EXPECTED_ARGS_MISSING_EQUALS = {
        "--accesslog/bogus/path/to/hourly_test_access.log",
        "--startDate=2017-01-01.00:00:00",
        "--duration=hourly",
        "--threshold=200"
    };

    @Test
    public void testGetArgsArray() {
        actualArgs = new CommandLineArgsEmulator.Builder()
                .duration(HOURLY.toString())
                .pathToLogFile(BOGUS_TEST_LOG_FILE_PATH)
                .startDate(TEST_START_DATE)
                .threshold(Integer.toString(THRESHOLD_200))
                .build().getEmulatedArgsArray();
        assertArrayEquals(EXPECTED_ARGS, actualArgs);
    }

    @Test
    public void testGetArgsArray_missingEquals() {
        actualArgs = new CommandLineArgsEmulator.Builder()
                .pathToLogFile(BOGUS_TEST_LOG_FILE_PATH)
                .startDate(TEST_START_DATE)
                .duration(HOURLY.toString())
                .threshold(Integer.toString(THRESHOLD_200))
                .leaveOutEqualsSign(true)
                .build().getEmulatedArgsArray();
        assertArrayEquals(EXPECTED_ARGS_MISSING_EQUALS, actualArgs);
    }

    @Test
    public void testGetArgsArray_emptyArray() {
        actualArgs = new CommandLineArgsEmulator.Builder()
                                .build().getEmulatedArgsArray();
        assertArrayEquals(new String[]{}, actualArgs);
    }

}
