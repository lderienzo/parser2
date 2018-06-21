package com.ef;

import static com.ef.arguments.Args.ARG_HANDLER_MAP;
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
import com.google.common.base.Strings;


public class Parser {

    public static void main(String... params) {
        try {
            Map<String, String> argsMap = Args.getMap(params);
            if (allRequiredArgsPresent(argsMap, params)) {

                LocalDateTime startDate = ARG_HANDLER_MAP.get(START_DATE)
                        .getValue(argsMap.get(START_DATE.toString()), LocalDateTime.class);

                Duration duration = ARG_HANDLER_MAP.get(DURATION)
                        .getValue(argsMap.get(DURATION.toString()), Duration.class);

                int threshold = ARG_HANDLER_MAP.get(THRESHOLD)
                        .getValue(argsMap.get(THRESHOLD.toString()), Integer.class);

                LogHandler logHandler = initDbHandler();
                readFileIfPathPresent(argsMap, logHandler);
                Map<Long, Long> blockedIps =
                        logHandler.getBlockedIps(startDate, duration, threshold);
                logHandler.saveBlockedIps(blockedIps, startDate, duration, threshold);
                System.out.println(logHandler.getBlockedIpsMessage(blockedIps));

                System.exit(0);
            }
            else {
                throw new ArgsException("Missing required argument(s).");
            }
        } catch (ArgsException e) {
            System.out.println("Error processing command line arguments. Please re-enter.");
            System.out.println(Args.getUsage());
            System.err.println(e.errorMessage());
        }
    }

    private static boolean allRequiredArgsPresent(Map<String, String> argsMap, String... params) {
        return (argsMap != null && argsMap.size() == params.length);
    }

    private static void readFileIfPathPresent(Map<String, String> argsMap, LogHandler logHandler) throws ArgsException {
        String filePath = "";
        if (argsMap.containsKey(ACCESS_LOG.toString())) {
            filePath = ARG_HANDLER_MAP.get(ACCESS_LOG)
                    .getValue(argsMap.get(ACCESS_LOG.toString()), String.class);
        }
        if (!Strings.isNullOrEmpty(filePath)) {
            logHandler.read(filePath);
        }
    }

    private static LogHandler initDbHandler() {
        ParserApplication db =
                new ParserApplicationBuilder().withPassword(DB_PWD).build();

        AccessLogEntryManager logEntryManager = db.getOrThrow(AccessLogEntryManager.class);
        BlockedIpManager blockedIpManager = db.getOrThrow(BlockedIpManager.class);
        return new AccessLogHandler(logEntryManager, blockedIpManager);
    }
}
