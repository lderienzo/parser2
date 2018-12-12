/*
 * Created by Luke DeRienzo on 10/31/18 12:30 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 10/31/18 12:26 PM
 */

package com.ef.serveraccesslogentrystore;

import static com.ef.arguments.Args.ARG_HANDLER_MAP;
import static com.ef.arguments.Args.ArgName.DURATION;
import static com.ef.arguments.Args.ArgName.START_DATE;
import static com.ef.arguments.Args.ArgName.THRESHOLD;

import java.time.LocalDateTime;
import java.util.Map;

import com.ef.arguments.Args;
import com.ef.enums.Duration;

public final class SearchCriteria {
    private final LocalDateTime startDate;
    private final Duration duration;
    private final int threshold;

    private SearchCriteria(Map<String,String> argsMap) {
        this.startDate = getValidValue(argsMap, START_DATE, LocalDateTime.class);
        this.duration = getValidValue(argsMap, DURATION, Duration.class);
        this.threshold = getValidValue(argsMap, THRESHOLD, Integer.class);
    }

    public static SearchCriteria createFromArgsMap(Map<String,String> argsMap) {
        return new SearchCriteria(argsMap);
    }

    private <T> T getValidValue(Map<String,String> argsMap, Args.ArgName arg, Class<T> returnValueClass) {
        return ARG_HANDLER_MAP.get(arg).getValue(argsMap.get(arg.toString()), returnValueClass);
    }

    public LocalDateTime startDate() {
        return startDate;
    }

    public Duration duration() {
        return duration;
    }

    public int threshold() {
        return threshold;
    }
}
