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
import com.speedment.runtime.core.exception.SpeedmentException;


public class Parser {

    public static void main(String... params) {
        ParserApplication db = null;
        try {
            Map<String,String> argsMap = Args.getMap(params);
            if (requiredArgsPresent(argsMap, params)) {
                LocalDateTime startDate = ARG_HANDLER_MAP.get(START_DATE)
                        .getValue(argsMap.get(START_DATE.toString()), LocalDateTime.class);

                Duration duration = ARG_HANDLER_MAP.get(DURATION)
                        .getValue(argsMap.get(DURATION.toString()), Duration.class);

                int threshold = ARG_HANDLER_MAP.get(THRESHOLD)
                        .getValue(argsMap.get(THRESHOLD.toString()), Integer.class);

                String filePath = ARG_HANDLER_MAP.get(ACCESS_LOG)
                        .getValue(argsMap.get(ACCESS_LOG.toString()), String.class);

                db = initDb();
                LogHandler logHandler = initLogHandler(db);
                if (isValid(filePath)) {
                    logHandler.read(filePath);
                }
                Map<Long,Long> blockedIps =
                        logHandler.getBlockedIps(startDate, duration, threshold);
                logHandler.saveBlockedIps(blockedIps, startDate, duration, threshold);
                System.out.println(logHandler.getBlockedIpsMessage(blockedIps));
            }
            else {
                throw new ArgsException("Missing required argument(s).");
            }
        } catch (SpeedmentException|ArgsException e) {
            e.printStackTrace(System.out);
            System.out.println("\n"+Args.getUsage());
        }
        finally {
            if (db != null) {
                db.close();
            }
        }
    }

    private static boolean requiredArgsPresent(Map<String,String> argsMap, String... params) {
        return (argsMap != null && argsMap.size() == params.length);
    }

    private static ParserApplication initDb() {
        return new ParserApplicationBuilder().withPassword(DB_PWD).build();
    }

    private static LogHandler initLogHandler(ParserApplication db) {
        AccessLogEntryManager logEntryManager = db.getOrThrow(AccessLogEntryManager.class);
        BlockedIpManager blockedIpManager = db.getOrThrow(BlockedIpManager.class);
        return new AccessLogHandler(logEntryManager, blockedIpManager);
    }

    private static boolean isValid(String path) {
        return !Strings.isNullOrEmpty(path);
    }
}
