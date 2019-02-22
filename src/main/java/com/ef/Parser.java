/*
 * Created by Luke DeRienzo on 8/15/18 12:29 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 7/30/18 1:41 PM
 */

package com.ef;

import static com.ef.arguments.enums.Args.ACCESS_LOG;
import static com.ef.arguments.enums.Args.DURATION;
import static com.ef.arguments.enums.Args.START_DATE;
import static com.ef.arguments.enums.Args.THRESHOLD;
import static com.ef.constants.Constants.PARSER_BLOCKED_IPS_FOUND_MSG;
import static com.ef.constants.Constants.STORE_PASSWORD;

import java.util.List;

import com.ef.arguments.ArgsException;
import com.ef.arguments.ArgsProcessor;
import com.ef.arguments.validation.ValidatedArgs;
import com.ef.blockedipstore.SpeedmentBlockedIpStore;
import com.ef.blockedipstore.SearchCriteria;
import com.ef.blockedipstore.BlockedIpStore;
import com.ef.utils.IpAddressConverter;
import com.google.common.base.Strings;
import com.speedment.runtime.core.exception.SpeedmentException;

final class Parser {
    private List<Long> ipsToBlock;
    private String accesslog;
    private SearchCriteria ipBlockingCriteria;
    private final BlockedIpStore logEntryStore;
    private static final String USAGE_MESSAGE = "\nUsage:\n" +
            " \tcom.ef.Parser [--"+ACCESS_LOG+"=<path_to_log_file>] " +
            "--"+START_DATE+"=<begin_looking_from_this_date_and_time> " +
            "(--"+DURATION+"=<hourly>|<daily>) --"+THRESHOLD+"=<1-500>\n";

    public Parser(BlockedIpStore logEntryStore) {
        this.logEntryStore = logEntryStore;
    }

    public static void main(String... withUserSuppliedArgs) {
        new Parser(usingSpeedmentBlockedIpStore())
                .run(withUserSuppliedArgs);
    }

    private static SpeedmentBlockedIpStore usingSpeedmentBlockedIpStore() {
        return SpeedmentBlockedIpStore.getSingletonInstance(STORE_PASSWORD);
    }

    public final void run(String... args) {
        runArgProcessing(args);
        runBlockedIpProcessing();
    }

    private void runArgProcessing(String... args) {
        try {
            processArgs(args);
        } catch (ArgsException e) {
            printErrorAndUsageToConsoleAndReThrow(e);
        }
    }

    private void processArgs(String... args) {
        ValidatedArgs validatedArgs = new ArgsProcessor().process(args);
        setAccessLogPathWithValidatedValue(validatedArgs);
        setIpBlockingCriteriaWithValidatedValues(validatedArgs);
    }

    private void setAccessLogPathWithValidatedValue(ValidatedArgs validatedArgs) {
        accesslog = validatedArgs.getAccessLog();
    }

    private void setIpBlockingCriteriaWithValidatedValues(ValidatedArgs validatedArgs) {
        ipBlockingCriteria = validatedArgs.getIpBlockingSearchCriteria();
    }

    private void printErrorAndUsageToConsoleAndReThrow(Exception e) {
        printOnlyFirstLineOfStackTraceAndErrorMessage(e);
        System.out.println(USAGE_MESSAGE);
        throw new ArgsException("Parser Error", e);
    }

    private void printOnlyFirstLineOfStackTraceAndErrorMessage(Exception e) {
        StackTraceElement[] elements = e.getStackTrace();
        System.err.println(elements[0] + " " + e.getMessage());
    }

    private void runBlockedIpProcessing() {
        try {
            processLogEntries();
        } catch (SpeedmentException e) {
            printErrorAndUsageToConsoleAndReThrow(e);
        }
        finally {
            closeDb();
        }
        printBlockedIps();
    }

    private void processLogEntries() {
        loadLogFileIntoStore();
        searchStoreForIpsToBlock();
        saveFoundIpsToBlock();
    }

    private void loadLogFileIntoStore() {
        if (logFilePathIsPresent())
            logEntryStore.loadFile(accesslog);
    }

    private boolean logFilePathIsPresent() {
        return !Strings.isNullOrEmpty(accesslog);
    }

    private void searchStoreForIpsToBlock() {
        ipsToBlock = logEntryStore.findIpsToBlock(ipBlockingCriteria);
    }

    private void saveFoundIpsToBlock() {
        if (ipsWhereFound())
            logEntryStore.saveIpsToBlock(ipBlockingCriteria, ipsToBlock);
    }

    private boolean ipsWhereFound() {
        return (ipsToBlock != null && !ipsToBlock.isEmpty());
    }

    private void closeDb() {
        if (logEntryStore != null)
            logEntryStore.shutdownStore();
    }

    private void printBlockedIps() {
        System.out.println(PARSER_BLOCKED_IPS_FOUND_MSG);
        if (ipsWhereFound())
            printThem();
        else
            System.out.println("* NONE *");
    }

    private void printThem() {
        ipsToBlock.stream().map(IpAddressConverter::fromLongToString).forEach(System.out::println);
    }
}
