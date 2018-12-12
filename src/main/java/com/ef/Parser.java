/*
 * Created by Luke DeRienzo on 8/15/18 12:29 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 7/30/18 1:41 PM
 */

package com.ef;

import static com.ef.arguments.Args.ARG_HANDLER_MAP;
import static com.ef.arguments.Args.ArgName.ACCESS_LOG;
import static com.ef.arguments.Args.ArgName.DURATION;
import static com.ef.arguments.Args.ArgName.START_DATE;
import static com.ef.arguments.Args.ArgName.THRESHOLD;
import static com.ef.utils.ParserUtils.DB_PWD;

import java.util.List;
import java.util.Map;

import com.ef.arguments.Args;
import com.ef.arguments.ArgsException;
import com.ef.db.ParserApplication;
import com.ef.db.ParserApplicationBuilder;
import com.ef.db.parser.parser.access_log_entry.AccessLogEntryManager;
import com.ef.db.parser.parser.blocked_ip.BlockedIpManager;
import com.ef.serveraccesslogentrystore.LogEntryStore;
import com.ef.serveraccesslogentrystore.SearchCriteria;
import com.ef.serveraccesslogentrystore.ServerAccessLogEntryStore;
import com.ef.utils.IpAddressConverter;
import com.google.common.base.Strings;
import com.speedment.runtime.core.exception.SpeedmentException;


public final class Parser {
    private static final String USAGE_MESSAGE = "\nUsage:\n" +
            " \tcom.ef.Parser [--"+ACCESS_LOG+"=<path_to_log_file>] " +
            "--"+START_DATE+"=<begin_looking_from_this_date_and_time> " +
            "(--"+DURATION+"=<hourly>|<daily>) --"+THRESHOLD+"=<100-500>\n";

    public static void main(String... commandLineArgs) {
        ParserApplication db = null;
        try { // args needs to be "processed" part of which is "vetting/validating" them before they're used.
            // Then transforming their values from String to the appropriate data type so the app can  more efficiently work with them.
            Map<String,String> args = Args.getArgsMap(commandLineArgs);
            if (allRequiredArgsPresent(args, commandLineArgs)) {
                db = initDb();
                ServerAccessLogEntryStore<Long> logEntryStore = initServerAccessLogEntryStore(db);
                String logPath = getValidValue(args, ACCESS_LOG, String.class);
                if (isValid(logPath)) {
                    logEntryStore.loadFile(logPath);
                }

                SearchCriteria reason = SearchCriteria.createFromArgsMap(args);
                List<Long> ips = logEntryStore.findIpsToBlock(reason);
                logEntryStore.saveIpsToBlock(reason, ips);
                printBlockedIps(ips);
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

    private static void printBlockedIps(List<Long> ips) {
        System.out.println("Blocked Ips For Entered Criteria:");
        ips.stream().map(IpAddressConverter::toIp).forEach(System.out::println);
    }

    private static boolean allRequiredArgsPresent(Map<String,String> args, String... commandLineArgs) {
        return (args != null && args.size() == commandLineArgs.length);
    }

    private static <T> T getValidValue(Map<String,String> args, Args.ArgName arg, Class<T> returnValueClass) {
        return ARG_HANDLER_MAP.get(arg).getValue(args.get(arg.toString()), returnValueClass);
    }

    private static ParserApplication initDb() {
        return new ParserApplicationBuilder().withPassword(DB_PWD).build();
    }

    private static ServerAccessLogEntryStore<Long> initServerAccessLogEntryStore(ParserApplication db) {
        AccessLogEntryManager logEntryManager = db.getOrThrow(AccessLogEntryManager.class);
        BlockedIpManager blockedIpManager = db.getOrThrow(BlockedIpManager.class);
        return new LogEntryStore(logEntryManager, blockedIpManager);
    }

    private static boolean isValid(String path) {
        return !Strings.isNullOrEmpty(path);
    }

    public static String getUsage() {
        return USAGE_MESSAGE;
    }



}
