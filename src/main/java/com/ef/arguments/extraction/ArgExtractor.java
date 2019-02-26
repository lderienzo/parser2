/*
 * Created by Luke DeRienzo on 2/25/19 9:34 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 2/5/19 12:35 PM
 */

package com.ef.arguments.extraction;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.ef.arguments.enums.Args;
import com.ef.arguments.ArgsException;

import static com.ef.arguments.enums.Args.*;
import static com.ef.constants.Constants.ARG_EXTRACTOR_DUPLICATE_ARGS_ERR_MSG_PREFIX;
import static com.ef.constants.Constants.ARG_EXTRACTOR_DUPLICATE_ARGS_ERR_MSG_SUFFIX;

public final class ArgExtractor {
    private List<String> extractedArgList;
    private Args argName;

    public ExtractedArgs extractFromCommandLine(String... args) {
        return new ExtractedArgs.Builder()
                .accesslog(extractSingleArgFromStream(ACCESS_LOG, args))
                .startDate(extractSingleArgFromStream(START_DATE, args))
                .duration(extractSingleArgFromStream(DURATION, args))
                .threshold(extractSingleArgFromStream(THRESHOLD, args))
                .build();
    }

    private String extractSingleArgFromStream(Args argName, String... args) {
        this.argName = argName;
        extractedArgList = getSingleElementArgListFromArrayStream(args);
        if (argListIsNullOrEmpty())
            return "";
        throwExceptionIfArgListContainsMoreThanOneValue();
        return extractedArgList.get(0);
    }

    private List<String> getSingleElementArgListFromArrayStream(String... args) {
        return Arrays.stream(args)
                .filter(arg -> arg.contains(argName.toString()))
                .map(arg -> arg.substring(arg.indexOf('=') + 1))
                .collect(Collectors.toList());
    }

    private boolean argListIsNullOrEmpty() {
        return extractedArgList == null || extractedArgList.isEmpty();
    }

    private void throwExceptionIfArgListContainsMoreThanOneValue() {
        if (extractedArgList != null && extractedArgList.size() != 1)
            throw new ArgsException(ARG_EXTRACTOR_DUPLICATE_ARGS_ERR_MSG_PREFIX + argName.toString()
                    + ARG_EXTRACTOR_DUPLICATE_ARGS_ERR_MSG_SUFFIX);
    }
}