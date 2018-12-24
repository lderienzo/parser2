/*
 * Created by Luke DeRienzo on 12/10/18 9:01 AM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 12/10/18 9:00 AM
 */

package com.ef.arguments.extractor;


import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.ef.arguments.Args;
import com.ef.arguments.ArgsException;
import com.google.common.base.Strings;

import static com.ef.arguments.Args.ArgName.*;

public final class ArgExtractor {
    private static final String ERROR_MSG_PREFIX = "Failure in ArgExtractor::";
    private List<String> extractedArgList;

    public ExtractedArgs extract(String... args) {
        return new ExtractedArgs.Builder()
                .accesslog(extractFromStream(ACCESS_LOG, args))
                .startDate(extractFromStream(START_DATE, args))
                .duration(extractFromStream(DURATION, args))
                .threshold(extractFromStream(THRESHOLD, args))
                .build();
    }

    private String extractFromStream(Args.ArgName argName, String... args) {
        extractedArgList = getListFromStream(argName, args);
        if (argValueAbsent())
            return "";
        checkArgListOfSizeOneOrThrowException(argName);
        return extractedArgList.get(0);
    }

    private List<String> getListFromStream(Args.ArgName argName, String... args) {
        return Arrays.stream(args)
                .filter(arg -> arg.contains(argName.toString()))
                .map(arg -> arg.substring(arg.indexOf('=') + 1))
                .collect(Collectors.toList());
    }

    private boolean argValueAbsent() {
        return extractedArgList == null || extractedArgList.isEmpty();
    }

    private void checkArgListOfSizeOneOrThrowException(Args.ArgName argName) {
        if (extractedArgList != null && extractedArgList.size() != 1)
            throw new ArgsException(ERROR_MSG_PREFIX + "checkArgListOfSizeOneOrThrowException. " +
                    "Error extracting argument ["+argName+"]. Single value required. Please see application 'Usage'.");
    }











}

/*
--accesslog=/path/to/file --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100

need to determine which args are present
 */