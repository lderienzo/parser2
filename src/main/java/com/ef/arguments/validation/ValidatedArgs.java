/*
 * Created by Luke DeRienzo on 12/12/18 11:14 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 12/12/18 11:14 PM
 */

package com.ef.arguments.validation;

import static com.ef.enums.Duration.DAILY;
import static com.ef.enums.Duration.HOURLY;

import java.time.LocalDateTime;

import com.ef.arguments.extraction.ExtractedArgs;
import com.ef.enums.Duration;
import com.ef.serveraccesslogentrystore.SearchCriteria;
import com.ef.utils.ParserUtils;

public final class ValidatedArgs {
    private int threshold;
    private Duration duration;
    private LocalDateTime startDate;
    private SearchCriteria ipBlockingCriteria;
    private String accessLog, startDateStr, durationStr, thresholdStr;


    public ValidatedArgs(String accessLog, SearchCriteria ipBlockingCriteria) {
        this.accessLog = accessLog;
        this.ipBlockingCriteria = ipBlockingCriteria;
    }

    public ValidatedArgs(ExtractedArgs extractedArgs) {
        setArgStringRepresentations(extractedArgs);
        convertArgsToProperTypes();
        createIpBlockingCriteriaWithTypeConvertedArgs();
    }

    private void setArgStringRepresentations(ExtractedArgs extractedArgs) {
        this.accessLog = extractedArgs.getAccesslog();
        this.startDateStr = extractedArgs.getStartDate();
        this.durationStr = extractedArgs.getDuration();
        this.thresholdStr = extractedArgs.getThreshold();
    }

    private void convertArgsToProperTypes() {
        convertStartDateFromStringToLclDtTime();
        convertDurationFromStringToEnum();
        convertThresholdFromStringToInt();
    }

    private void convertStartDateFromStringToLclDtTime() {
        startDate = ParserUtils.stringToLocalDateTime(startDateStr);
    }

    private void convertDurationFromStringToEnum() {
        duration = (durationStr.equals(HOURLY.toString()) ? HOURLY : DAILY);
    }

    private void convertThresholdFromStringToInt() {
        threshold = ParserUtils.stringToInt(thresholdStr);
    }

    private void createIpBlockingCriteriaWithTypeConvertedArgs() {
        ipBlockingCriteria = new SearchCriteria(startDate, duration, threshold);
    }

    public SearchCriteria getIpBlockingSearchCriteria() {
        return ipBlockingCriteria;
    }

    public String getAccessLog() {
        return accessLog;
    }
}
