package com.ef;

import static com.ef.arguments.Args.ARG_PROCESSING_MAP;
import static com.ef.arguments.Args.ArgName.ACCESS_LOG;
import static com.ef.arguments.Args.ArgName.DURATION;
import static com.ef.arguments.Args.ArgName.START_DATE;
import static com.ef.arguments.Args.ArgName.THRESHOLD;
import static com.ef.utils.ParserUtils.DB_PWD;

import java.time.LocalDateTime;
import java.util.Map;

import com.ef.arguments.Args;
import com.ef.arguments.ArgsException;
import com.ef.db.ParserApplication;
import com.ef.db.ParserApplicationBuilder;
import com.ef.db.parser.parser.access_log_entry.AccessLogEntryManager;
import com.ef.db.parser.parser.blocked_ip.BlockedIpManager;
import com.ef.enums.Duration;
import com.ef.loghandler.AccessLogHandler;
import com.ef.loghandler.LogHandler;
import com.ef.utils.ParserUtils;
import com.google.common.base.Strings;


public class Parser {

    public static void main(String... params) {
        Map<String, String> argsMap = Args.process(params);
        if (argsMap == null || argsMap.isEmpty() || argsMap.size() != params.length) {
            System.err.println("Error entering required application arguments. Please re-enter.");
            return;
        }

        // set defaults
        String filePath = "";
        LocalDateTime startDate = LocalDateTime.now();
        Duration duration = Duration.DAILY;
        int threshold = ParserUtils.THRESHOLD_100;
        try {
            filePath = ARG_PROCESSING_MAP
                    .get(ACCESS_LOG.toString())
                    .getValue(argsMap.get(ACCESS_LOG.toString()), String.class);

            startDate = ARG_PROCESSING_MAP
                    .get(START_DATE.toString())
                    .getValue(argsMap.get(START_DATE.toString()), LocalDateTime.class);

            duration = ARG_PROCESSING_MAP
                    .get(DURATION.toString())
                    .getValue(argsMap.get(DURATION.toString()), Duration.class);

            threshold = ARG_PROCESSING_MAP
                    .get(THRESHOLD.toString())
                    .getValue(argsMap.get(THRESHOLD.toString()), Integer.class);

        } catch (ArgsException e) {
            System.err.println("Error processing command line arguments. Please re-enter.");
            System.err.println(e.errorMessage());
            System.out.println();
            System.out.println(Args.getUsage());
        }

        LogHandler logHandler = initDbHandler();
        if (!Strings.isNullOrEmpty(filePath)) {
            logHandler.read(filePath);
        }

        Map<Long, Long> blockedIps = logHandler.getBlockedIps(startDate, duration, threshold);
        String saveStatus = logHandler.saveBlockedIps(blockedIps, startDate, duration, threshold);
        System.out.println(saveStatus);
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
