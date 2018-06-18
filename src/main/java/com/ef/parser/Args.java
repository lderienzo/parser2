package com.ef.parser;

import static com.ef.parser.Args.ArgName.ACCESS_LOG;
import static com.ef.parser.Args.ArgName.DURATION;
import static com.ef.parser.Args.ArgName.START_DATE;
import static com.ef.parser.Args.ArgName.THRESHOLD;

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

    public static final ImmutableMap<String, ArgHandler> ARG_PROCESSING_MAP =
            new ImmutableMap.Builder<String, ArgHandler>()
                    .put(ACCESS_LOG.toString(), new FilePathArgHandler())
                    .put(START_DATE.toString(), new DateArgHandler())
                    .put(DURATION.toString(), new DurationArgHandler())
                    .put(THRESHOLD.toString(), new IntArgHandler())
                    .build();

    public static Map<String, String> process(String... params) {
        Map<String, String> enteredArgs = Arrays.stream(params)
                .map(str -> str.replace("--", ""))
                .map(str -> str.split("="))
                .collect(
                        Collectors.toMap(
                            str -> str[0], str -> str[1]
                        ));

        for (Map.Entry<String, ArgHandler> argName : ARG_PROCESSING_MAP.entrySet()) {
            //!argName.getKey().equals(ACCESS_LOG.toString()) &&
            if (!enteredArgs.containsKey(argName.getKey())) {
                System.out.println("Missing argument: --"+argName.getKey());
                System.out.println(getUsage());
                enteredArgs.clear();
                break;
            }
        }
        return enteredArgs;
    }

    public static String getUsage() {
        return "Usage:\n" +
                " \tcom.ef.Parser --accesslog=<path_to_log_file> " +
                "--startDate=<begin_looking_from_this_date_and_time> " +
                "(--duration=<hourly>|<daily>) --threshold=<100-500>";
    }
}