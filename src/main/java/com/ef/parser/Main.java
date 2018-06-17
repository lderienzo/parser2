package com.ef.parser;

import static com.ef.parser.ParserUtils.DB_PWD;
import static com.ef.parser.ParserUtils.LOG_FILE_NAME;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ef.parser.db.ParserApplication;
import com.ef.parser.db.ParserApplicationBuilder;
import com.ef.parser.db.parser.parser.access_log_entry.AccessLogEntryManager;
import com.google.common.collect.ImmutableSet;
import com.speedment.runtime.core.ApplicationBuilder;


public class Main {

    public static void main(String... params) throws FileNotFoundException {
//        // initialize db connection...
        ParserApplication db = new ParserApplicationBuilder() // initialize db connection...
                .withPassword(DB_PWD)
                .withLogging(ApplicationBuilder.LogType.PERSIST)
                .withLogging(ApplicationBuilder.LogType.STREAM)
                .build();

        Map<String, Object> argsMap = parseArgs(params);
        String logFilePath = (String)argsMap.get(LOG_FILE_NAME);

        readLogFileIntoDb(db, logFilePath);

    }

    private static void readLogFileIntoDb(ParserApplication db, String pathToLogFile) {
        AccessLogEntryManager logEntryManager = db.getOrThrow(AccessLogEntryManager.class);
        LogReader logReader = new AccessLogReader(logEntryManager);
        logReader.read(pathToLogFile);
    }

    // TODO: temporary. Come up with a more robust and elegant way of doing this.
    private static Map<String, Object> parseArgs(String... params) throws FileNotFoundException {

        // check to make sure all required command line args are present
        String paramsStr = String.join(",", params);
        if ( !paramsStr.contains("accesslog") || !paramsStr.contains("startDate")
                || !paramsStr.contains("duration") || !paramsStr.contains("threshold")) {
            throw new IllegalArgumentException("Error: misspelled or missing argument(s). " +
                    "Usage --accesslog=[/path/to/file.log] --startDate=[yyyy-MM-dd.HH:mm:ss] " +
                    "--duration=[hourly,daily] --threshold=[positive integer]");
        }

        List<String> argValuePairs = Arrays.stream(params)
//                .map(str -> str.split("="))
//                .flatMap(Arrays::stream)
                .map(str -> str.replace("--", ""))
                .collect(Collectors.toList());
//                .forEach(System.out::println);

        Map<String, Object> argsMap = new HashMap<>();
        for (String argValuePair : argValuePairs) {
            String[] pair = argValuePair.split("=");
            argsMap.put(pair[0], pair[1]);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");
        for (String key : argsMap.keySet()) {
            if (key.equals("accesslog")) {
                String logPath = (String)argsMap.get(key);
                if (logPath == null) {
                    throw new IllegalArgumentException("Log file path value is null!");
                }
                File file = new File(logPath);
                if (!file.exists() || !file.isFile()) {
                    throw new FileNotFoundException("Log file does not exist!");
                }
            }
            else if (key.equals("startDate")) {
                String startDateStr = (String)argsMap.get(key);
                if (startDateStr == null) {
                    throw new IllegalArgumentException("startDate value is null!");
                }
                LocalDateTime startDate = LocalDateTime.parse(startDateStr, formatter);
                argsMap.put(key, startDate);
            }
            else if (key.equals("duration")) {
                String duration = (String)argsMap.get(key);
                if (duration == null) {
                    throw new IllegalArgumentException("duration value is null!");
                }
                if (!"hourly|daily".contains(duration)) {
                    throw new IllegalArgumentException("duration does not equal 'hourly' or 'daily'");
                }
            }
            else if (key.equals("threshold")) {
                String thresholdStr = (String)argsMap.get(key);
                if (thresholdStr == null) {
                    throw new IllegalArgumentException("threshold value is null!");
                }
                int threshold = Integer.parseInt(thresholdStr);
                if (threshold < 100 || threshold > 500) { // set arbitrary lower and upper bounds on threshold
                    throw new IllegalArgumentException("threshold must be between 100-500");
                }
                argsMap.put(key, threshold);
            }
        }
        return argsMap;
    }

//    private Function<String, CommandLineArgument> mapToCommandLineArgument = (String argument) -> {
//        Arrays.stream(argument.split("=")).collect(Collectors.toList())
//                .forEach(System.out::println);
//        return null;
//    };
}
