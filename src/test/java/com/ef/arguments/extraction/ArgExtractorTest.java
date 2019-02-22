/*
 * Created by Luke DeRienzo on 12/10/18 2:38 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 12/10/18 2:04 PM
 */

package com.ef.arguments.extraction;

import static com.ef.arguments.enums.Args.ACCESS_LOG;
import static com.ef.constants.Constants.ARG_EXTRACTOR_DUPLICATE_ARGS_ERR_MSG_PREFIX;
import static com.ef.constants.Constants.ARG_EXTRACTOR_DUPLICATE_ARGS_ERR_MSG_SUFFIX;
import static com.ef.arguments.enums.Duration.HOURLY;
import static com.ef.utils.ParserTestUtils.*;
import static org.junit.Assert.assertEquals;


import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.ef.arguments.ArgsException;
import com.ef.arguments.CommandLineArgsEmulator;

public final class ArgExtractorTest {
    private String[] emulatedArgs;
    private final ArgExtractor argExtractor = new ArgExtractor();
    private ExtractedArgs extractedArgs;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void testArgExtractor_allArgsExtractedWhenAllArgsPresent(){
        emulatedArgs = getEmulatedArgsWithAllPresent();

        extractedArgs = argExtractor.extractFromCommandLine(emulatedArgs);

        checkThatAllExtractedArgsArePresent();
    }

    private String[] getEmulatedArgsWithAllPresent() {
        return new CommandLineArgsEmulator.Builder()
                .accesslog(TEST_ACCESS_LOG_FILE)
                .startDate(HOURLY_TEST_START_DATE)
                .duration(HOURLY.toString())
                .threshold(Integer.toString(THRESHOLD_200))
                .build().getEmulatedArgsArray();
    }

    private void checkThatAllExtractedArgsArePresent() {
        assertEquals(ASSERT_EQUALS_MSG, TEST_ACCESS_LOG_FILE, extractedArgs.getAccesslog());
        assertEquals(ASSERT_EQUALS_MSG, HOURLY_TEST_START_DATE, extractedArgs.getStartDate());
        assertEquals(ASSERT_EQUALS_MSG, HOURLY.toString(), extractedArgs.getDuration());
        assertEquals(ASSERT_EQUALS_MSG, Integer.toString(THRESHOLD_200), extractedArgs.getThreshold());
    }

    @Test
    public void testArgExtractor_noArgsExtractedWhenNoArgsPresent() {
        emulatedArgs = getEmptyEmulatedArgs();

        extractedArgs = argExtractor.extractFromCommandLine(emulatedArgs);

        checkThatAllExtractedArgsAreBlank();
    }

    private String[] getEmptyEmulatedArgs() {
        return new CommandLineArgsEmulator.Builder().build().getEmulatedArgsArray();
    }

    private void checkThatAllExtractedArgsAreBlank() {
        assertEquals(ASSERT_EQUALS_MSG, "", extractedArgs.getAccesslog());
        assertEquals(ASSERT_EQUALS_MSG, "", extractedArgs.getStartDate());
        assertEquals(ASSERT_EQUALS_MSG, "", extractedArgs.getDuration());
        assertEquals(ASSERT_EQUALS_MSG, "", extractedArgs.getThreshold());
    }

    @Test
    public void testArgExtractor_noArgsExtractedWhenSingleIncorrectlyFormattedArgPresent() {
        String[] invalidFormatArg = {"al;dkjfadfasdfa**!"};

        extractedArgs = argExtractor.extractFromCommandLine(invalidFormatArg);

        checkThatAllExtractedArgsAreBlank();
    }

    @Test
    public void testArgExtractor_singleArgExtractedWhenOnlySingleArgPresent() {
        emulatedArgs = getEmulateArgsWithOnlySingleArgPresent();

        extractedArgs = argExtractor.extractFromCommandLine(emulatedArgs);

        assertEquals(ASSERT_EQUALS_MSG, TEST_ACCESS_LOG_FILE, extractedArgs.getAccesslog());
        assertEquals(ASSERT_EQUALS_MSG, "", extractedArgs.getStartDate());
        assertEquals(ASSERT_EQUALS_MSG, "", extractedArgs.getDuration());
        assertEquals(ASSERT_EQUALS_MSG, "", extractedArgs.getThreshold());
    }

    private String[] getEmulateArgsWithOnlySingleArgPresent() {
        return new CommandLineArgsEmulator.Builder()
                .accesslog(TEST_ACCESS_LOG_FILE)
                .build().getEmulatedArgsArray();
    }

    @Test
    public void testArgExtractor_exceptionThrownWhenDuplicateArgsPresent() {
        emulatedArgs = getEmulatedArgsContainingOnlyDuplicatedArg();
        thrown.expect(ArgsException.class);
        thrown.expectMessage(ARG_EXTRACTOR_DUPLICATE_ARGS_ERR_MSG_PREFIX
                + ACCESS_LOG.toString() + ARG_EXTRACTOR_DUPLICATE_ARGS_ERR_MSG_SUFFIX);

        argExtractor.extractFromCommandLine(emulatedArgs);
    }

    private String[] getEmulatedArgsContainingOnlyDuplicatedArg() {
        emulatedArgs = getEmulateArgsWithOnlySingleArgPresent();
        addDuplicateArgToArray();
        return emulatedArgs;
    }

    private void addDuplicateArgToArray() {
        emulatedArgs = Arrays.copyOf(emulatedArgs,2);
        Arrays.fill(emulatedArgs,1,2, getEmulateArgsWithOnlySingleArgPresent()[0]);
    }
}
