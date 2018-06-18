package com.ef.parser;

import static com.ef.parser.Args.ARG_PROCESSING_MAP;
import static com.ef.parser.Args.ArgName.ACCESS_LOG;
import static com.ef.parser.Args.ArgName.DURATION;
import static com.ef.parser.Args.ArgName.START_DATE;
import static com.ef.parser.Args.ArgName.THRESHOLD;
import static com.ef.parser.ParserUtils.DB_PWD;

import java.time.LocalDateTime;
import java.util.Map;

import com.ef.parser.db.ParserApplication;
import com.ef.parser.db.ParserApplicationBuilder;
import com.ef.parser.db.parser.parser.access_log_entry.AccessLogEntryManager;
import com.ef.parser.db.parser.parser.blocked_ip.BlockedIpManager;
import com.google.common.base.Strings;


public class Parser {

    public static void main(String... params) {
        Map<String, String> argsMap = Args.process(params);
        if (argsMap == null || argsMap.isEmpty() || argsMap.size() != params.length) {
            System.err.println("Error entering required application arguments. Please try again.");
            return;
        }

        ArgHandler<String> pathArgHandler = ARG_PROCESSING_MAP.get(ACCESS_LOG.toString());
        String filePath = "";
        try {
            filePath = pathArgHandler.getValue(argsMap.get(ACCESS_LOG.toString()));
        } catch (ArgsException e) {
            System.err.println("Error processing log file path. Please try again.");
            System.err.println(e.errorMessage());
            System.out.println();
            System.out.println(Args.getUsage());
        }

        LogHandler logHandler = initDbHandler();
        if (!Strings.isNullOrEmpty(filePath)) {
            logHandler.read(filePath);
        }

        ArgHandler<LocalDateTime> dateArgHandler = ARG_PROCESSING_MAP.get(START_DATE.toString());
        ArgHandler<Duration> durationArgHandler = ARG_PROCESSING_MAP.get(DURATION.toString());
        ArgHandler<Integer> intArgHandler = ARG_PROCESSING_MAP.get(THRESHOLD.toString());
        Map<Long, Long> blockedIps = null;
        String saveStatus = "";
        try {
            blockedIps = logHandler.getBlockedIps(
                    dateArgHandler.getValue(argsMap.get(START_DATE.toString())),
                    durationArgHandler.getValue(DURATION.toString()),
                    intArgHandler.getValue(argsMap.get(THRESHOLD.toString()))
            );

            saveStatus = logHandler.saveBlockedIps(
                    blockedIps,
                    dateArgHandler.getValue(argsMap.get(START_DATE.toString())),
                    durationArgHandler.getValue(DURATION.toString()),
                    intArgHandler.getValue(argsMap.get(THRESHOLD.toString()))
            );
        } catch (ArgsException e) {
            System.err.println("Error processing application arguments. Please try again.");
            System.err.println(e.errorMessage());
        }
        finally {
            System.out.println(saveStatus);
        }

        System.out.println(logHandler.getBlockedIpsMessage(blockedIps));

        System.exit(0);
    }

    private static LogHandler initDbHandler() {
        ParserApplication db = new ParserApplicationBuilder()
                .withPassword(DB_PWD)
                .build();

        AccessLogEntryManager logEntryManager = db.getOrThrow(AccessLogEntryManager.class);
        BlockedIpManager blockedIpManager = db.getOrThrow(BlockedIpManager.class);
        return new AccessLogHandler(logEntryManager, blockedIpManager);
    }
}
