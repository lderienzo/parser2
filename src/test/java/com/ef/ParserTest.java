/*
 * Created by Luke DeRienzo on 11/10/18 4:49 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 11/10/18 4:49 PM
 */

package com.ef;

import static com.ef.constants.TestConstants.EXPECTED_MULTIPLE_BLOCKED_IPS_FOR_HOUR;
import static com.ef.constants.TestConstants.IP_MATCHING_REGEX;
import static com.ef.arguments.enums.Duration.HOURLY;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.*;

import com.ef.arguments.CommandLineArgsEmulator;
import com.ef.blockedIpstore.config.SpeedmentBlockedIpStoreForTesting;
import com.ef.config.TestConfig;
import com.ef.constants.TestConstants;
import com.ef.utils.*;


public class ParserTest {
    private static Parser parser;
    private static final SpeedmentBlockedIpStoreForTesting TEST_SPEEDMENT_BLOCKED_IP_STORE = TestConfig.getTestStore();


    @Rule
    public final SystemOutResource consoleOutput = new SystemOutResource();


    @BeforeClass
    public static void createParserAppObjectWithStore() {
        parser = new Parser(TEST_SPEEDMENT_BLOCKED_IP_STORE);
    }

    @AfterClass
    public static void clearDB() {
        TEST_SPEEDMENT_BLOCKED_IP_STORE.shutDownStore();
    }

    @Test
    public void testParser_runningWithAllArgsPresentAndValidShouldFindIpsToBlockAndSaveThem() {

        parser.run(getArgsForFindingSixIpsToBlock());

        List<Long> actualBlockedIps = extractActualBlockedIpsFromConsoleOutput();
        assertEquals(EXPECTED_MULTIPLE_BLOCKED_IPS_FOR_HOUR, actualBlockedIps);
    }

    private String[] getArgsForFindingSixIpsToBlock() {
        return new CommandLineArgsEmulator.Builder()
                .accesslog(TestConstants.VALID_TEST_LOG_FILE_PATH)
                .startDate(TestConstants.TEST_START_DATE)
                .duration(HOURLY.toString())
                .threshold(TestConstants.THRESHOLD_FOR_FINDING_SIX_IPS_IN_HOUR)
                .build().getEmulatedArgsArray();
    }

    private List<Long> extractActualBlockedIpsFromConsoleOutput() {
        return Stream.of(consoleOutput.toString().split("\n"))
                .filter(elem -> elem.matches(IP_MATCHING_REGEX))
                .map(IpAddressConverter::fromStringToLong)
                .collect(Collectors.toList());
    }
}