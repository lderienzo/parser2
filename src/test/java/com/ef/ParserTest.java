/*
 * Created by Luke DeRienzo on 11/10/18 4:49 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 11/10/18 4:49 PM
 */

package com.ef;

import static com.ef.constants.Constants.IP_MATCHING_REGEX;
import static com.ef.arguments.enums.Duration.HOURLY;
import static com.ef.utils.ParserTestUtils.*;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.*;

import com.ef.arguments.CommandLineArgsEmulator;
import com.ef.blockedIpstore.ParserBlockedIpStoreAbstractParentForTesting;
import com.ef.utils.*;


public class ParserTest extends ParserBlockedIpStoreAbstractParentForTesting {
    private static Parser parser;
    // TODO: REPEATED - CODE consolidate (keep "D.R.Y.")
    private final List<Long> expectedMultipleBlockedIpsForHour =
            Stream.of(3232245325L, 3232248781L, 3232272305L,
                    3232278978L, 3232287599L, 3232295506L).collect(
                    Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

    @Rule
    public final SystemOutResource consoleOutput = new SystemOutResource();


    @BeforeClass
    public static void createParserAppObjectWithStore() {
        parser = new Parser(testSpeedmentBlockedIpStore);
    }

    @Test
    public void testParser_runningWithAllArgsPresentAndValidShouldFindIpsToBlockAndSaveThem() {

        parser.run(getArgsForFindingSixIpsToBlock());

        List<Long> actualBlockedIps = extractActualBlockedIpsFromConsoleOutput();
        assertEquals(expectedMultipleBlockedIpsForHour, actualBlockedIps);
    }

    private String[] getArgsForFindingSixIpsToBlock() {
        return new CommandLineArgsEmulator.Builder()
                .accesslog(VALID_TEST_LOG_FILE_PATH)
                .startDate(TEST_START_DATE)
                .duration(HOURLY.toString())
                .threshold(THRESHOLD_FOR_FINDING_SIX_IPS_IN_HOUR)
                .build().getEmulatedArgsArray();
    }

    private List<Long> extractActualBlockedIpsFromConsoleOutput() {
        return Stream.of(consoleOutput.toString().split("\n"))
                .filter(elem -> elem.matches(IP_MATCHING_REGEX))
                .map(IpAddressConverter::fromStringToLong)
                .collect(Collectors.toList());
    }
}