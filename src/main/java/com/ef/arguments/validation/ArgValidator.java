/*
 * Created by Luke DeRienzo on 12/12/18 8:55 AM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 12/12/18 8:55 AM
 */

package com.ef.arguments.validation;

import static com.ef.constants.Constants.INVALID_ACCESSLOG_PATH_ERR_MSG;
import static com.ef.constants.Constants.THRESHOLD_CONVERTER_OUTSIDE_ALLOWABLE_RANGE_ERR_MSG;

import java.io.File;
import java.time.LocalDateTime;

import com.ef.arguments.ArgsException;
import com.ef.arguments.conversion.DurationConverter;
import com.ef.arguments.conversion.StartDateConverter;
import com.ef.arguments.conversion.ThresholdConverter;
import com.ef.arguments.extraction.ExtractedArgs;
import com.ef.enums.Duration;
import com.ef.serveraccesslogentrystore.SearchCriteria;
import com.google.common.base.Strings;

public final class ArgValidator {
    private ExtractedArgs extractedArgs;
    private SearchCriteria searchCriteriaForBlockingIps;
    private LocalDateTime startDate;
    private Integer threshold;
    private Duration duration;
    private boolean requiredArgsValidated;


    public ValidatedArgs validate(ExtractedArgs extractedArgs) {
        setExtractedArgsToBeValidated(extractedArgs);
        validateRequiredArgs();
        validateNonRequiredArgs();
        return createValidatedArgsObjectWithValidatedData();
    }

    private void setExtractedArgsToBeValidated(ExtractedArgs extractedArgs) {
        this.extractedArgs = extractedArgs;
    }

    private void validateRequiredArgs() {
        validateStartDate();
        validateDuration();
        validateThreshold();
        requiredArgsValidated = true;
    }

    private void validateStartDate() {
        startDateValidIfCanBeLocalDateTimeObject();
    }

    private void startDateValidIfCanBeLocalDateTimeObject() {
        convertStartDateToLocalDateTime();
    }

    private void convertStartDateToLocalDateTime() {
        startDate = new StartDateConverter().convert(extractedArgs.getStartDate());
    }

    private void validateDuration() {
        durationValidIfCanBeDurationEnum();
    }

    private void durationValidIfCanBeDurationEnum() {
        convertDurationToDurationEnum();
    }

    private void convertDurationToDurationEnum() {
        duration = new DurationConverter().convert(extractedArgs.getDuration());
    }

    private void validateThreshold() {
        thresholdIsValidIfItCanBeAnInteger();
        thresholdIsValidIfInAllowableRange();
    }

    private void thresholdIsValidIfItCanBeAnInteger() {
        convertThresholdToInteger();
    }

    private void convertThresholdToInteger() {
        threshold = new ThresholdConverter().convert(extractedArgs.getThreshold());
    }

    private void thresholdIsValidIfInAllowableRange() {
        if (thresholdIsOutsideOfAllowableRange())
            throw new ArgsException(THRESHOLD_CONVERTER_OUTSIDE_ALLOWABLE_RANGE_ERR_MSG);
    }

    private boolean thresholdIsOutsideOfAllowableRange() {
        return (threshold < 1 || threshold > 500);
    }

    private void validateNonRequiredArgs() {
        validateAccessLog();
    }

    private void validateAccessLog() {
        if (accessLogIsPresent())
            checkPathValidity();
    }

    private boolean accessLogIsPresent() {
        return argValueIsPresent(extractedArgs.getAccesslog());
    }

    private boolean argValueIsPresent(String arg) {
        return !argValueIsAbsent(arg);
    }

    private boolean argValueIsAbsent(String arg) {
        return Strings.isNullOrEmpty(arg);
    }

    private void checkPathValidity() {
        if (fileDoesNotExist())
            throw new ArgsException(INVALID_ACCESSLOG_PATH_ERR_MSG);
    }

    private boolean fileDoesNotExist() {
        return !fileExists();
    }

    private boolean fileExists() {
        File file = new File(extractedArgs.getAccesslog());
        return (file.exists() && file.isFile());
    }

    private ValidatedArgs createValidatedArgsObjectWithValidatedData() {
        createSearchCriteriaWithValidatedRequiredArgs();
        return new ValidatedArgs(extractedArgs.getAccesslog(), searchCriteriaForBlockingIps);
    }

    private void createSearchCriteriaWithValidatedRequiredArgs() {
        if (requiredArgsValidated)
            searchCriteriaForBlockingIps = new SearchCriteria(startDate, duration, threshold);
    }
}
