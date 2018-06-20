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

    public static final ImmutableMap<ArgName, ArgHandler> ARG_HANDLER_MAP =
            new ImmutableMap.Builder<ArgName, ArgHandler>()
                    .put(ACCESS_LOG, new FilePathArgHandler())
                    .put(START_DATE, new DateArgHandler())
                    .put(DURATION, new DurationArgHandler())
                    .put(THRESHOLD, new IntArgHandler())
                    .build();

    public static Map<String, String> getMap(String... params) throws ArgsException {
        Map<String, String> enteredArgs = Arrays.stream(params)
                .map(str -> str.replace("--", ""))
                .map(str -> str.split("="))
                .filter(strArr -> strArr.length == 2)
                .collect(
                        Collectors.toMap(
                            str -> str[0], str -> str[1]
                        ));

        for (Map.Entry<ArgName, ArgHandler> argName : ARG_HANDLER_MAP.entrySet()) {
            if (missingRequiredArg(argName, enteredArgs)) {
                System.out.println("Missing required argument: --" + argName.getKey());
                enteredArgs.clear();
                throw new ArgsException("Missing required argument.");
            }
        }
        return enteredArgs;
    }

    private static boolean missingRequiredArg(Map.Entry<ArgName, ArgHandler> argName, Map<String, String> enteredArgs) {
        return (!enteredArgs.containsKey(argName.getKey().toString()) && !argName.getKey().equals(ACCESS_LOG));
    }

    public static String getUsage() {
        return "Usage:\n" +
                " \tcom.ef.Parser --"+ACCESS_LOG+"=<path_to_log_file> " +
                "--"+START_DATE+"=<begin_looking_from_this_date_and_time> " +
                "(--"+DURATION+"=<hourly>|<daily>) --"+THRESHOLD+"=<100-500>\n";
    }
}
