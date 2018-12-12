/*
 * Created by Luke DeRienzo on 10/31/18 12:30 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 10/31/18 12:26 PM
 */

package com.ef.serveraccesslogentrystore;

import static com.ef.enums.Duration.DAILY;
import static com.speedment.runtime.field.predicate.Inclusion.START_INCLUSIVE_END_INCLUSIVE;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ef.db.parser.parser.access_log_entry.AccessLogEntry;
import com.ef.db.parser.parser.access_log_entry.AccessLogEntryImpl;
import com.ef.db.parser.parser.access_log_entry.AccessLogEntryManager;
import com.ef.db.parser.parser.blocked_ip.BlockedIpImpl;
import com.ef.db.parser.parser.blocked_ip.BlockedIpManager;
import com.ef.enums.Duration;
import com.ef.loghandler.LogHandlerException;
import com.ef.utils.IpAddressConverter;
import com.ef.utils.ParserUtils;
import com.speedment.runtime.core.exception.SpeedmentException;

public final class LogEntryStore implements ServerAccessLogEntryStore<Long> {
    private static final String DELIMITER = "\\|";
    private final AccessLogEntryManager logEntryManager;
    private final BlockedIpManager blockedIpManager;

    public LogEntryStore(AccessLogEntryManager logEntryManager, BlockedIpManager blockedIpManager) {
        this.logEntryManager = logEntryManager;
        this.blockedIpManager = blockedIpManager;
    }

    @Override
    public void loadFile(String path) {
        try (Stream<String> lines = Files.lines(Paths.get(path))) {
            lines.map(mapToAccessLogEntry).forEach(logEntryManager.persister());
        }
        catch(IOException e) {
            throw new LogHandlerException("Failure in LogEntryStore::loadFile. Error reading log file.", e);
        }
        catch(SpeedmentException e){
            throw new LogHandlerException("Failure in LogEntryStore::loadFile. Error loading log file into database.", e);
        }
    }

    private final Function<String,AccessLogEntry> mapToAccessLogEntry = (line) -> {
        String[] entry = line.split(DELIMITER);
        return new AccessLogEntryImpl()
            .setDate(LocalDateTime.parse(entry[0], ParserUtils.LOG_FILE_DATE_FORMATTER))
            .setIpAddress(IpAddressConverter.toLong(entry[1]))
            .setRequest(entry[2])
            .setStatus(Integer.parseInt(entry[3]))
            .setUserAgent(entry[4]);
    };

    @Override
    public List<Long> findIpsToBlock(SearchCriteria blockingCriteria) {
        LocalDateTime startDate = blockingCriteria.startDate();
        LocalDateTime endDate = startDate.plusHours(1);  // default to hourly duration
        if (blockingCriteria.duration() == DAILY) {
            endDate = startDate.plusDays(1);
        }

        List<Long> iPsToBlock;
        try {
            iPsToBlock = logEntryManager.stream()
                    .filter(AccessLogEntry.DATE.between(startDate, endDate, START_INCLUSIVE_END_INCLUSIVE))
                    .collect(
                        Collectors.groupingBy(
                            AccessLogEntry::getIpAddress,
                            Collectors.counting()
                        )
                    )
                    .entrySet()
                    .stream()
                    .filter(e -> e.getValue().intValue() >= blockingCriteria.threshold())
                    .map(Map.Entry::getKey)
                    .sorted()
                .collect(Collectors.toList());
        } catch (SpeedmentException e) {
            throw new LogHandlerException("Failure in LogEntryStore::findIpsToBlock. Error finding blocked IPs in database table.", e);
        }
        return iPsToBlock;
    }

    @Override
    public void saveIpsToBlock(SearchCriteria blockingCriteria, List<Long> iPsToBlock) {
        String comment = createCommentDescribingReasonForBlocking(blockingCriteria);
        try {
            iPsToBlock.stream()
                .map(ip ->
                    new BlockedIpImpl()
                        .setIpAddress(ip)
                        .setComment(comment))
                .forEach(blockedIpManager.persister());
        } catch (SpeedmentException e) {
            throw new LogHandlerException("Failure in LogEntryStore::saveIpsToBlock. Error writing blocked IPs to database.", e);
        }
    }

    private final String createCommentDescribingReasonForBlocking(SearchCriteria blockingCriteria) {
        return new StringBuilder("IP exceeded max of [")
                .append(blockingCriteria.threshold()).append("] ")
                .append("allowed requests in more than ")
                .append(blockingCriteria.duration() == Duration.HOURLY ? "an" : "a").append(" [")
                .append(blockingCriteria.duration().toString()).append("] ")
                .append("period starting at [")
                .append(blockingCriteria.startDate().toString()).append("].")
            .toString();
    }
}
