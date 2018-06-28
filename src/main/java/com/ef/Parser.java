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
                LocalDateTime startDate = getValidValue(argsMap, START_DATE, LocalDateTime.class);
                Duration duration = getValidValue(argsMap, DURATION, Duration.class);
                int threshold = getValidValue(argsMap, THRESHOLD, Integer.class);
                String file = getValidValue(argsMap, ACCESS_LOG, String.class);

                db = initDb();
                LogHandler logHandler = initLogHandler(db);
                if (isValid(file)) {
                    logHandler.save(file);
                }
                Map<Long,Long> blockedIps =
                        logHandler.getBlockedIps(startDate, duration, threshold);
                logHandler.saveBlockedIps(blockedIps, startDate, duration, threshold);
                System.out.println(logHandler.getBlockedIpsMessage(blockedIps));
            }
            else {
                throw new ArgsException("Failure in Parser main method: missing required argument.");
            }
        } catch (SpeedmentException|ArgsException e) {
            e.printStackTrace(System.out);
            System.out.println(getUsage());
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

    private static <T> T getValidValue(Map<String,String> argsMap, Args.ArgName arg, Class<T> returnValueClass) {
        return ARG_HANDLER_MAP.get(arg).getValue(argsMap.get(arg.toString()), returnValueClass);
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

    public static String getUsage() {
        return "\nUsage:\n" +
                " \tcom.ef.Parser [--"+ACCESS_LOG+"=<path_to_log_file>] " +
                "--"+START_DATE+"=<begin_looking_from_this_date_and_time> " +
                "(--"+DURATION+"=<hourly>|<daily>) --"+THRESHOLD+"=<100-500>\n";
    }
}
