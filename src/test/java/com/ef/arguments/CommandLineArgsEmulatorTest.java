/*
 * Created by Luke DeRienzo on 2/25/19 9:34 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 2/24/19 12:16 PM
 */

package com.ef.arguments;

import static com.ef.constants.TestConstants.TEST_ACCESS_LOG_FILE;
import static com.ef.constants.TestConstants.TEST_START_DATE;

import org.junit.Test;

import static com.ef.arguments.enums.Duration.HOURLY;
import static com.ef.constants.TestConstants.THRESHOLD_200;
import static org.junit.Assert.assertArrayEquals;


public final class CommandLineArgsEmulatorTest {
    private String[] actualArgs;
    private static final String[] EXPECTED_ARGS =
    {
        "--accesslog=test_access.log",
        "--startDate=2017-01-01.00:00:00",
        "--duration=hourly",
        "--threshold=200"
    };
    private static final String[] EXPECTED_ARGS_MISSING_EQUALS =
    {
        "--accesslogtest_access.log",
        "--startDate=2017-01-01.00:00:00",
        "--duration=hourly",
        "--threshold=200"
    };


    @Test
    public void testGetArgsArray() {

        actualArgs = new CommandLineArgsEmulator.Builder()
            .duration(HOURLY.toString())
            .accesslog(TEST_ACCESS_LOG_FILE)
            .startDate(TEST_START_DATE)
            .threshold(Integer.toString(THRESHOLD_200))
            .build().getEmulatedArgsArray();

        assertArrayEquals(EXPECTED_ARGS, actualArgs);
    }

    @Test
    public void testGetArgsArray_missingEquals() {

        actualArgs = new CommandLineArgsEmulator.Builder()
            .accesslog(TEST_ACCESS_LOG_FILE)
            .startDate(TEST_START_DATE)
            .duration(HOURLY.toString())
            .threshold(Integer.toString(THRESHOLD_200))
            .leaveOutEqualsSign(true)
            .build().getEmulatedArgsArray();

        assertArrayEquals(EXPECTED_ARGS_MISSING_EQUALS, actualArgs);
    }

    @Test
    public void testGetArgsArray_emptyArray() {

        actualArgs = new CommandLineArgsEmulator
                        .Builder().build().getEmulatedArgsArray();

        assertArrayEquals(new String[]{}, actualArgs);
    }
}
