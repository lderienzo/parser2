/*
 * Created by Luke DeRienzo on 12/12/18 11:01 AM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 12/12/18 11:01 AM
 */

package com.ef.arguments.validation;

import static com.ef.constants.Constants.*;
import static com.ef.arguments.enums.Duration.HOURLY;
import static com.ef.constants.TestConstants.ASSERT_EQUALS_MSG;
import static com.ef.constants.TestConstants.BOGUS_TEST_LOG_FILE_PATH;
import static com.ef.constants.TestConstants.HOURLY_TEST_START_DATE;
import static com.ef.constants.TestConstants.HOURLY_TEST_START_DATE_AFTER_CONVERSION_TO_LCL_DT_TIME;
import static com.ef.constants.TestConstants.INVALID_VALUE;
import static com.ef.constants.TestConstants.THRESHOLD_200;
import static com.ef.constants.TestConstants.VALID_TEST_LOG_FILE_PATH;
import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.ef.arguments.ArgsException;
import com.ef.arguments.extraction.ExtractedArgs;
import com.ef.arguments.enums.Duration;
import com.ef.blockedipstore.SearchCriteria;

public final class ArgValidatorTest {
    private ExtractedArgs extractedArgs;
    private ValidatedArgs validatedArgs;
    private SearchCriteria expectedSearchCriteria;
    private static ArgValidator argValidator;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void setUp() {
        argValidator = new ArgValidator();
    }

    @Test
    public void testValidate_allRequiredArgsPresentAndValidProduceProperlyConvertedValues() {
        extractedArgs = givenAllRequiredAndValidArgsForSearchCriteria();

        validatedArgs = argValidator.validate(extractedArgs);

        expectedSearchCriteria = validatedArgs.getIpBlockingSearchCriteria();
        assertTrue(expectedSearchCriteria.startDate() instanceof LocalDateTime);
        assertTrue(expectedSearchCriteria.duration() instanceof Duration);
        assertEquals(expectedSearchCriteria.threshold(), THRESHOLD_200);
    }

    private ExtractedArgs givenAllRequiredAndValidArgsForSearchCriteria() {
        return new ExtractedArgs.Builder()
                .startDate(HOURLY_TEST_START_DATE)
                .threshold(Integer.toString(THRESHOLD_200))
                .duration(HOURLY.toString())
                .build();
    }

    @Test
    public void testValidate_allArgsPresentAndValidProducesValidatedArgsWithProperValues() {
        extractedArgs = givenAllArgsWithValidValues();

        validatedArgs = argValidator.validate(extractedArgs);

        expectedSearchCriteria = validatedArgs.getIpBlockingSearchCriteria();
        assertEquals(ASSERT_EQUALS_MSG, VALID_TEST_LOG_FILE_PATH, validatedArgs.getAccessLog());
        assertEquals(ASSERT_EQUALS_MSG, expectedSearchCriteria.duration(), HOURLY);
        assertEquals(ASSERT_EQUALS_MSG, expectedSearchCriteria.threshold(), THRESHOLD_200);
        assertEquals(ASSERT_EQUALS_MSG, expectedSearchCriteria.startDate().toString(),
                HOURLY_TEST_START_DATE_AFTER_CONVERSION_TO_LCL_DT_TIME);
    }

    private ExtractedArgs givenAllArgsWithValidValues() {
        return new ExtractedArgs.Builder()
                .accesslog(VALID_TEST_LOG_FILE_PATH)
                .startDate(HOURLY_TEST_START_DATE)
                .threshold(Integer.toString(THRESHOLD_200))
                .duration(HOURLY.toString())
                .build();
    }

    @Test
    public void testValidate_throwArgExceptionWhenRequiredStartDateIsAbsent() {
        extractedArgs = givenArgsWithMissingStartDateValue();
        thrown.expect(ArgsException.class);
        thrown.expectMessage(STRING_TYPE_CONVERTER_VALUE_ABSENT_ERR_MSG);

        validatedArgs = argValidator.validate(extractedArgs);
    }

    private ExtractedArgs givenArgsWithMissingStartDateValue() {
        return new ExtractedArgs.Builder()
                .accesslog(VALID_TEST_LOG_FILE_PATH)
                .threshold(Integer.toString(THRESHOLD_200))
                .duration(HOURLY.toString())
                .build();
    }

    @Test
    public void testValidate_throwArgExceptionWhenRequiredDurationIsAbsent() {
        extractedArgs = givenArgsWithMissingDurationValue();
        thrown.expect(ArgsException.class);
        thrown.expectMessage(STRING_TYPE_CONVERTER_VALUE_ABSENT_ERR_MSG);

        validatedArgs = argValidator.validate(extractedArgs);
    }

    private ExtractedArgs givenArgsWithMissingDurationValue() {
        return new ExtractedArgs.Builder()
                .accesslog(VALID_TEST_LOG_FILE_PATH)
                .startDate(HOURLY_TEST_START_DATE)
                .threshold(Integer.toString(THRESHOLD_200))
                .build();
    }

    @Test
    public void testValidate_throwArgExceptionWhenRequiredThresholdIsAbsent() {
        extractedArgs = givenArgsWithMissingThresholdValue();
        thrown.expect(ArgsException.class);
        thrown.expectMessage(STRING_TYPE_CONVERTER_VALUE_ABSENT_ERR_MSG);

        validatedArgs = argValidator.validate(extractedArgs);
    }

    private ExtractedArgs givenArgsWithMissingThresholdValue() {
        return new ExtractedArgs.Builder()
                .accesslog(VALID_TEST_LOG_FILE_PATH)
                .startDate(HOURLY_TEST_START_DATE)
                .duration(HOURLY.toString())
                .build();
    }

    @Test
    public void testValidate_throwArgExceptionWhenRequiredStartDateIsInvalid() {
        extractedArgs = givenArgsWithInvalidStartDateValue();
        thrown.expect(ArgsException.class);
        thrown.expectMessage(START_DATE_CONVERTER_ERR_MSG_PREFIX
                + INVALID_VALUE + START_DATE_CONVERTER_ERR_MSG_SUFFIX);

        validatedArgs = argValidator.validate(extractedArgs);
    }

    private ExtractedArgs givenArgsWithInvalidStartDateValue() {
        return new ExtractedArgs.Builder()
                .accesslog(VALID_TEST_LOG_FILE_PATH)
                .startDate(INVALID_VALUE)
                .threshold(Integer.toString(THRESHOLD_200))
                .duration(HOURLY.toString())
                .build();
    }

    @Test
    public void testValidate_throwArgExceptionWhenRequiredDurationIsInvalid() {
        extractedArgs = givenArgsWithInvalidDurationValue();
        thrown.expect(ArgsException.class);
        thrown.expectMessage(DURATION_CONVERTER_ERR_MSG);

        validatedArgs = argValidator.validate(extractedArgs);
    }

    private ExtractedArgs givenArgsWithInvalidDurationValue() {
        return new ExtractedArgs.Builder()
                .accesslog(VALID_TEST_LOG_FILE_PATH)
                .startDate(HOURLY_TEST_START_DATE)
                .threshold(Integer.toString(THRESHOLD_200))
                .duration(INVALID_VALUE)
                .build();
    }

    @Test
    public void testValidate_throwArgExceptionWhenRequiredThresholdIsInvalid() {
        extractedArgs = givenArgsWithInvalidThresholdValue();
        thrown.expect(ArgsException.class);
        thrown.expectMessage(THRESHOLD_CONVERTER_ERR_MSG_PREFIX
                + INVALID_VALUE + THRESHOLD_CONVERTER_ERR_MSG_SUFFIX);

        validatedArgs = argValidator.validate(extractedArgs);
    }

    private ExtractedArgs givenArgsWithInvalidThresholdValue() {
        return new ExtractedArgs.Builder()
                .accesslog(VALID_TEST_LOG_FILE_PATH)
                .startDate(HOURLY_TEST_START_DATE)
                .threshold(INVALID_VALUE)
                .duration(HOURLY.toString())
                .build();
    }

    @Test
    public void testValidate_throwArgExceptionWhenRequiredThresholdIsOutsideAllowableRange() {
        extractedArgs = givenArgsWithOutOfRangeThresholdValue();
        thrown.expect(ArgsException.class);
        thrown.expectMessage(THRESHOLD_CONVERTER_OUTSIDE_ALLOWABLE_RANGE_ERR_MSG);

        validatedArgs = argValidator.validate(extractedArgs);
    }

    private ExtractedArgs givenArgsWithOutOfRangeThresholdValue() {
        return new ExtractedArgs.Builder()
                .accesslog(VALID_TEST_LOG_FILE_PATH)
                .startDate(HOURLY_TEST_START_DATE)
                .threshold("0")
                .duration(HOURLY.toString())
                .build();
    }

    @Test
    public void testValidate_throwArgExceptionWhenAccessLogValueIsInvalid() {
        extractedArgs = givenArgsWithInvalidAccessLogValue();
        thrown.expect(ArgsException.class);
        thrown.expectMessage(INVALID_ACCESS_LOG_PATH_ERR_MSG);

        validatedArgs = argValidator.validate(extractedArgs);
    }

    private ExtractedArgs givenArgsWithInvalidAccessLogValue() {
        return new ExtractedArgs.Builder()
                .accesslog(BOGUS_TEST_LOG_FILE_PATH)
                .startDate(HOURLY_TEST_START_DATE)
                .threshold(Integer.toString(THRESHOLD_200))
                .duration(HOURLY.toString())
                .build();
    }
}
