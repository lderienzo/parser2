/*
 * Created by Luke DeRienzo on 12/10/18 2:38 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 12/10/18 2:04 PM
 */

package com.ef.arguments.extractor;

import static com.ef.enums.Duration.HOURLY;
import static com.ef.utils.ParserTestUtils.HOURLY_TEST_START_DATE;
import static com.ef.utils.ParserTestUtils.TEST_ACCESS_LOG_FILE;
import static com.ef.utils.ParserTestUtils.THRESHOLD_200;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import org.junit.Test;

import com.ef.arguments.ArgsException;
import com.ef.arguments.CommandLineArgsEmulator;

public class ArgExtractorTest {
    private String[] emulatedCommandLineArgs;
    private ArgExtractor argExtractor = new ArgExtractor();
    private ExtractedArgs extractedArgs;
    private static final String ASSERT_EQUALS_MSG = "Actual does not equal expected.";

    @Test
    public void testArgExtractor_allArgsPresent(){
        emulatedCommandLineArgs = getEmulatedArgsWithAllArgsPresent();
        extractedArgs = argExtractor.extract(emulatedCommandLineArgs);
        assertNotNull(extractedArgs);
        assertEquals(ASSERT_EQUALS_MSG, TEST_ACCESS_LOG_FILE, extractedArgs.getAccesslog());
        assertEquals(ASSERT_EQUALS_MSG, HOURLY_TEST_START_DATE, extractedArgs.getStartDate());
        assertEquals(ASSERT_EQUALS_MSG, HOURLY.toString(), extractedArgs.getDuration());
        assertEquals(ASSERT_EQUALS_MSG, Integer.toString(THRESHOLD_200), extractedArgs.getThreshold());
    }

    private String[] getEmulatedArgsWithAllArgsPresent() {
        return new CommandLineArgsEmulator.Builder()
                .pathToLogFile(TEST_ACCESS_LOG_FILE)
                .startDate(HOURLY_TEST_START_DATE)
                .duration(HOURLY.toString())
                .threshold(Integer.toString(THRESHOLD_200))
                .build().getEmulatedArgsArray();
    }

    @Test
    public void testArgExtractor_noArgsPresent() {
        emulatedCommandLineArgs = getEmulatedArgsNoArgsPresent();
        extractedArgs = argExtractor.extract(emulatedCommandLineArgs);
        assertNotNull(extractedArgs);
        checkThatAllArgsAreBlank();
    }

    private String[] getEmulatedArgsNoArgsPresent() {
        return new CommandLineArgsEmulator.Builder()
                .build().getEmulatedArgsArray();
    }

    private void checkThatAllArgsAreBlank() {
        assertEquals(ASSERT_EQUALS_MSG, "", extractedArgs.getAccesslog());
        assertEquals(ASSERT_EQUALS_MSG, "", extractedArgs.getStartDate());
        assertEquals(ASSERT_EQUALS_MSG, "", extractedArgs.getDuration());
        assertEquals(ASSERT_EQUALS_MSG, "", extractedArgs.getThreshold());
    }

    @Test
    public void testArgExtractor_invalidFormatArgPresent() {
        String[] invalidFormatArg = {"al;dkjfadfasdfa**!"};
        extractedArgs = argExtractor.extract(invalidFormatArg);
        assertNotNull(extractedArgs);
        checkThatAllArgsAreBlank();
    }

    @Test
    public void testArgExtractor_singleArgPresent() {
        emulatedCommandLineArgs = getEmulateArgsWithSingleArgPresent();
        extractedArgs = argExtractor.extract(emulatedCommandLineArgs);
        assertNotNull(extractedArgs);
        assertNotNull(extractedArgs.getAccesslog());
        assertEquals(ASSERT_EQUALS_MSG, TEST_ACCESS_LOG_FILE, extractedArgs.getAccesslog());
        assertEquals(ASSERT_EQUALS_MSG, "", extractedArgs.getStartDate());
        assertEquals(ASSERT_EQUALS_MSG, "", extractedArgs.getDuration());
        assertEquals(ASSERT_EQUALS_MSG, "", extractedArgs.getThreshold());
    }

    private String[] getEmulateArgsWithSingleArgPresent() {
        return new CommandLineArgsEmulator.Builder()
                .pathToLogFile(TEST_ACCESS_LOG_FILE)
                .build().getEmulatedArgsArray();
    }

    @Test(expected = ArgsException.class)
    public void testArgExtractor_duplicateArgsPresent() {
        emulatedCommandLineArgs = getEmulateArgsWithDuplicateArgsPresent();
        emulatedCommandLineArgs = Arrays.copyOf(emulatedCommandLineArgs,2);
        Arrays.fill(emulatedCommandLineArgs,1,2, getEmulateArgsWithDuplicateArgsPresent()[0]);
        argExtractor.extract(emulatedCommandLineArgs);
    }

    private String[] getEmulateArgsWithDuplicateArgsPresent() {
        return new CommandLineArgsEmulator.Builder()
                .pathToLogFile(TEST_ACCESS_LOG_FILE)
                .build().getEmulatedArgsArray();
    }
}
