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


    public static Map<String, String> getMap(String... params) throws ArgsException {
        Map<String,String> args = toMap(params);
        String missing = findMissingRequired(args);
        if (missing.isEmpty()) {
            return args;
        }
        else {
            args.clear();
            System.out.println("Missing required argument: "+missing+".");
            throw new ArgsException("Missing required argument");
        }
    }

    private static Map<String,String> toMap(String... params) {
        return Arrays.stream(params)
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
            if (isPresent(enteredArgs, argName)) {
                // continue...
            }
            else if (isRequired(argName)) {
                missingArg = argName.getKey().toString();
                break;
            }
        }
        return missingArg;
    }

    private static boolean isRequired(Map.Entry<ArgName,ArgHandler> argName) {
        return (argName.getKey().equals(THRESHOLD) ||
                argName.getKey().equals(START_DATE) || argName.getKey().equals(DURATION));
    }

    private static boolean isPresent(Map<String,String> enteredArgs,
                                        Map.Entry<ArgName,ArgHandler> argName) {
       return enteredArgs.containsKey(argName.getKey().toString());
    }

    public static String getUsage() {
        return "Usage:\n" +
                " \tcom.ef.Parser [--"+ACCESS_LOG+"=<path_to_log_file>] " +
                "--"+START_DATE+"=<begin_looking_from_this_date_and_time> " +
                "(--"+DURATION+"=<hourly>|<daily>) --"+THRESHOLD+"=<100-500>\n";
    }
}
