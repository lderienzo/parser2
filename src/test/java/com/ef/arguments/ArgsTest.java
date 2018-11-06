/*
 * Created by Luke DeRienzo on 8/15/18 12:29 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 6/20/18 7:09 PM
 */

package com.ef.arguments;

import static com.ef.arguments.Args.ARG_HANDLER_MAP;
import static com.ef.arguments.Args.ArgName.ACCESS_LOG;
import static com.ef.arguments.Args.ArgName.DURATION;
import static com.ef.arguments.Args.ArgName.START_DATE;
import static com.ef.arguments.Args.ArgName.THRESHOLD;
import static com.ef.enums.Duration.DAILY;
import static com.ef.enums.Duration.HOURLY;
import static com.ef.utils.ParserTestUtils.HOURLY_TEST_START_DATE;
import static com.ef.utils.ParserTestUtils.BOGUS_TEST_LOG_FILE_PATH;
import static com.ef.utils.ParserTestUtils.THRESHOLD_200;
import static com.ef.utils.ParserTestUtils.VALID_TEST_LOG_FILE_PATH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;

import com.ef.enums.Duration;


public final class ArgsTest {
    private String[] emulatedCommandLineArgs;
    private Map<String, String> args;

    @Test
    public void testGetMap_allArgsPresent() throws ArgsException {
        emulatedCommandLineArgs = new CommandLineArgsEmulator.Builder()
                .pathToLogFile(BOGUS_TEST_LOG_FILE_PATH)
                .startDate(HOURLY_TEST_START_DATE)
                .duration(HOURLY.toString())
                .threshold(Integer.toString(THRESHOLD_200))
                .build().getEmulatedArgsArray();
        args = Args.getArgsMap(emulatedCommandLineArgs);

        assertTrue(argsMapContainsExpectedData());
    }

    private boolean argsMapContainsExpectedData() {
        List<String[]> argPresenceVerificationList = Arrays.stream(emulatedCommandLineArgs)
                .map(arg -> arg.split("="))
                .filter(arg -> arg.length == 2)
                .filter(arg -> args.containsKey(arg[0].replace("--", "")))
                .filter(arg -> args.get(arg[0].replace("--", "")).equals(arg[1]))
                .collect(Collectors.toList());
        return argPresenceVerificationList.size() == args.size();
    }

    @Test
    public void testGetMap_allArgsPresentInDifferentOrder() throws ArgsException {
        emulatedCommandLineArgs = new CommandLineArgsEmulator.Builder()
                .threshold(Integer.toString(THRESHOLD_200))
                .duration(HOURLY.toString())
                .startDate(HOURLY_TEST_START_DATE)
                .pathToLogFile(BOGUS_TEST_LOG_FILE_PATH)
                .build().getEmulatedArgsArray();
        args = Args.getArgsMap(emulatedCommandLineArgs);

        assertTrue(argsMapContainsExpectedData());
    }

    @Test(expected = ArgsException.class)
    public void testGetMap_allArgsAbsent() throws ArgsException {
        String[] commandLineArgs = {};
        args = Args.getArgsMap(commandLineArgs);
        assertTrue(commandLineArgs.length == args.size());
    }

    @Test(expected = ArgsException.class)
    public void testGetMap_missingRequiredArg() throws ArgsException {
        emulatedCommandLineArgs = new CommandLineArgsEmulator.Builder()
                .pathToLogFile(BOGUS_TEST_LOG_FILE_PATH)
                .startDate(HOURLY_TEST_START_DATE)
                .threshold(Integer.toString(THRESHOLD_200))
                .build().getEmulatedArgsArray();
        Args.getArgsMap(emulatedCommandLineArgs);
    }

    @Test
    public void testGetMap_nonRequiredAccessLogPathAbsent() throws ArgsException {
        emulatedCommandLineArgs = new CommandLineArgsEmulator.Builder()
                .startDate(HOURLY_TEST_START_DATE)
                .duration(HOURLY.toString())
                .threshold(Integer.toString(THRESHOLD_200))
                .build().getEmulatedArgsArray();
        args = Args.getArgsMap(emulatedCommandLineArgs);

        assertTrue(argsMapContainsExpectedData());
    }

    @Test(expected = ArgsException.class)
    public void testGetMap_requiredAndNonRequiredArgsAbsent() throws ArgsException {
        emulatedCommandLineArgs = new CommandLineArgsEmulator.Builder()
                .startDate(HOURLY_TEST_START_DATE)
                .duration(HOURLY.toString())
                .build().getEmulatedArgsArray();
        Args.getArgsMap(emulatedCommandLineArgs);
    }

    @Test
    public void testGetMap_validAccessLogPath() throws ArgsException {
        emulatedCommandLineArgs = new CommandLineArgsEmulator.Builder()
                .pathToLogFile(VALID_TEST_LOG_FILE_PATH)
                .startDate(HOURLY_TEST_START_DATE)
                .duration(HOURLY.toString())
                .threshold(Integer.toString(THRESHOLD_200))
                .build().getEmulatedArgsArray();
        args = Args.getArgsMap(emulatedCommandLineArgs);
        String filePath = ARG_HANDLER_MAP.get(ACCESS_LOG)
                .getValue(args.get(ACCESS_LOG.toString()), String.class);

        assertEquals(VALID_TEST_LOG_FILE_PATH, filePath);
        assertTrue(argsMapContainsExpectedData());
    }

    @Test
    public void testGetMap_validLogPathButNoEqualsInItsArgFormat() throws ArgsException {
        emulatedCommandLineArgs = new CommandLineArgsEmulator.Builder()
                .pathToLogFile(VALID_TEST_LOG_FILE_PATH)
                .startDate(HOURLY_TEST_START_DATE)
                .duration(HOURLY.toString())
                .threshold(Integer.toString(THRESHOLD_200))
                .leaveOutEqualsSign(true)
                .build().getEmulatedArgsArray();
        args = Args.getArgsMap(emulatedCommandLineArgs);
        ARG_HANDLER_MAP.get(ACCESS_LOG)
                .getValue(args.get(ACCESS_LOG.toString()), String.class);

        assertTrue(argsMapContainsExpectedData());
    }

    @Test(expected = ArgsException.class)
    public void testGetMap_invalidStartDateFormatNoFilePath() throws ArgsException {
        emulatedCommandLineArgs = new CommandLineArgsEmulator.Builder()
                .startDate("2017-01-01~~~20:00:00")
                .duration(HOURLY.toString())
                .threshold(Integer.toString(THRESHOLD_200))
                .build().getEmulatedArgsArray();
        Map<String, String> argsMap = Args.getArgsMap(emulatedCommandLineArgs);
        ARG_HANDLER_MAP.get(START_DATE)
                .getValue(argsMap.get(START_DATE.toString()), LocalDateTime.class);
    }

    @Test
    public void testGetMap_validStartDateFormatAndDailyDurationNoFilePath() throws ArgsException {
        emulatedCommandLineArgs = new CommandLineArgsEmulator.Builder()
                .startDate(HOURLY_TEST_START_DATE)
                .duration(DAILY.toString())
                .threshold(Integer.toString(THRESHOLD_200))
                .build().getEmulatedArgsArray();
        args = Args.getArgsMap(emulatedCommandLineArgs);
        Duration duration = ARG_HANDLER_MAP.get(DURATION)
                .getValue(args.get(DURATION.toString()), Duration.class);

        assertEquals(DAILY, duration);
        assertTrue(argsMapContainsExpectedData());
    }

    @Test(expected = ArgsException.class)
    public void testGetMap_invalidDurationValue() throws ArgsException {
        emulatedCommandLineArgs = new CommandLineArgsEmulator.Builder()
                .startDate(HOURLY_TEST_START_DATE)
                .duration("boguz_value")
                .threshold(Integer.toString(THRESHOLD_200))
                .build().getEmulatedArgsArray();
        Map<String, String> argsMap = Args.getArgsMap(emulatedCommandLineArgs);
        ARG_HANDLER_MAP.get(DURATION)
                .getValue(argsMap.get(DURATION.toString()), Duration.class);
    }

    @Test(expected = ArgsException.class)
    public void testGetMap_belowRangeThreshold() throws ArgsException {
        emulatedCommandLineArgs = new CommandLineArgsEmulator.Builder()
                .startDate(HOURLY_TEST_START_DATE)
                .duration(DAILY.toString())
                .threshold("-1")
                .build().getEmulatedArgsArray();
        Map<String, String> argsMap = Args.getArgsMap(emulatedCommandLineArgs);
        ARG_HANDLER_MAP.get(THRESHOLD)
                .getValue(argsMap.get(THRESHOLD.toString()), Integer.class);
    }

    @Test(expected = ArgsException.class)
    public void testGetMap_aboveRangeThreshold() throws ArgsException {
        emulatedCommandLineArgs = new CommandLineArgsEmulator.Builder()
                .startDate(HOURLY_TEST_START_DATE)
                .duration(HOURLY.toString())
                .threshold("10001")
                .build().getEmulatedArgsArray();
        Map<String, String> argsMap = Args.getArgsMap(emulatedCommandLineArgs);
        ARG_HANDLER_MAP.get(THRESHOLD)
                .getValue(argsMap.get(THRESHOLD.toString()), Integer.class);
    }

    @Test(expected = ArgsException.class)
    public void
    testGetMap_nonIntThreshold() throws ArgsException {
        emulatedCommandLineArgs = new CommandLineArgsEmulator.Builder()
                .startDate(HOURLY_TEST_START_DATE)
                .duration(HOURLY.toString())
                .threshold("XYZ")
                .build().getEmulatedArgsArray();
        Map<String, String> argsMap = Args.getArgsMap(emulatedCommandLineArgs);
        ARG_HANDLER_MAP.get(THRESHOLD)
                .getValue(argsMap.get(THRESHOLD.toString()), Integer.class);
    }

    @Test
    public void testGetMap_extraBogusArgument() throws ArgsException {
        emulatedCommandLineArgs = new CommandLineArgsEmulator.Builder()
                .pathToLogFile(VALID_TEST_LOG_FILE_PATH)
                .startDate(HOURLY_TEST_START_DATE)
                .duration(HOURLY.toString())
                .threshold(Integer.toString(THRESHOLD_200))
                .extraBogusArg("some_extra_bogus_arg")
                .build().getEmulatedArgsArray();
        args = Args.getArgsMap(emulatedCommandLineArgs);

        assertTrue(argsMapContainsExpectedData());
    }
}