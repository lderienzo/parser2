/*
 * Created by Luke DeRienzo on 8/15/18 12:29 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 6/22/18 11:48 PM
 */

package com.ef.arguments;

import static com.ef.arguments.Args.ArgName.ACCESS_LOG;
import static com.ef.arguments.Args.ArgName.DURATION;
import static com.ef.arguments.Args.ArgName.START_DATE;
import static com.ef.arguments.Args.ArgName.THRESHOLD;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;

public class Args {
    // purpose of class -- commandline ArgsNew processing.
    // what does that mean? read in args, see if all required are present and valid
    // if not, raise exception, else, go on to convert each string ArgsNew to its appropriate
    // type to be used by main method


    public enum ArgName {
        ACCESS_LOG {
            @Override
            public String toString() {
                return "accesslog";
            }
        },
        START_DATE {
            @Override
            public String toString() {
                return "startDate";
            }
        },
        DURATION {
            @Override
            public String toString() {
                return "duration";
            }
        },
        THRESHOLD {
            @Override
            public String toString() {
                return "threshold";
            }
        }
    }

    public static final ImmutableMap<ArgName,ArgHandler> ARG_HANDLER_MAP =
            new ImmutableMap.Builder<ArgName,ArgHandler>()
                    .put(ACCESS_LOG, new FilePathArgHandler())
                    .put(START_DATE, new DateArgHandler())
                    .put(DURATION, new DurationArgHandler())
                    .put(THRESHOLD, new IntArgHandler())
                    .build();


    public static Map<String, String> getArgsMap(String... commandLineArgs) throws ArgsException {
        Map<String,String> args = toMap(commandLineArgs);
        String missing = findMissingRequired(args);
        if (missing.isEmpty()) {
            return args;
        }
        else {
            throw new ArgsException("Failure processing arguments in 'getArgsMap' method. " +
                    "Missing required argument ["+missing+"]");
        }
    }

    private static Map<String,String> toMap(String... commandLineArgs) {
        return Arrays.stream(commandLineArgs)
                .map(str -> str.replace("--", ""))
                .map(str -> str.split("="))
                .filter(strArr -> strArr.length == 2)
                .collect(
                    Collectors.toMap(
                            str -> str[0], str -> str[1]
                    ));
    }

    private static String findMissingRequired(Map<String,String> enteredArgs) {
        String missingArg = "";
        for (Map.Entry<ArgName,ArgHandler> argName : ARG_HANDLER_MAP.entrySet()) {
            if (isPresent(argName, enteredArgs)) {
                // continue...
            }
            else if (isRequired(argName)) {
                missingArg = argName.getKey().toString();
                break;
            }
        }
        return missingArg;
    }

    private static boolean isPresent(Map.Entry<ArgName,ArgHandler> argName,
                                     Map<String,String> enteredArgs) {
        return enteredArgs.containsKey(argName.getKey().toString());
    }

    private static boolean isRequired(Map.Entry<ArgName,ArgHandler> argName) {
        return (argName.getKey().equals(THRESHOLD) ||
                argName.getKey().equals(START_DATE) || argName.getKey().equals(DURATION));
    }
}
