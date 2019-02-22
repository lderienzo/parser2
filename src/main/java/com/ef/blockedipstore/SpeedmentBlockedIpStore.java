/*
 * Created by Luke DeRienzo on 1/23/19 9:03 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 1/23/19 9:03 PM
 */

package com.ef.blockedipstore;

import static com.ef.constants.Constants.LOG_ENTRY_STORE_ERR_LOADING_LOG_FILE;
import static com.ef.arguments.enums.Duration.DAILY;
import static com.speedment.runtime.field.predicate.Inclusion.START_INCLUSIVE_END_INCLUSIVE;

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
import com.ef.db.parser.parser.blocked_ip.BlockedIpImpl;
import com.ef.arguments.enums.Duration;
import com.ef.utils.IpAddressConverter;
import com.ef.utils.ParserUtils;
import com.speedment.runtime.core.exception.SpeedmentException;
import com.speedment.runtime.core.manager.Persister;

public class SpeedmentBlockedIpStore implements BlockedIpStore {
    private static final String DELIMITER = "\\|";
    private static SpeedmentStoreComponents speedmentStoreComponents;

    private SpeedmentBlockedIpStore() {
        speedmentStoreComponents =
            ParserStoreComponents.getSingletonInstance(SpeedmentStoreComponents.password);
    }

    private static class SingletonHolder {
        static final SpeedmentBlockedIpStore instance = new SpeedmentBlockedIpStore();
    }

    public static SpeedmentBlockedIpStore getSingletonInstance(String pwd) {
        SpeedmentStoreComponents.password = pwd;
        return SingletonHolder.instance;
    }

    @Override
    public void loadFile(String filePath) {
        saveFileContentsToStore(filePath);
    }

    private void saveFileContentsToStore(String filePath) {
        try (Stream<String> fileLines = covertFileContentsToStream(filePath)) {
            saveLinesAsAccessLogEntries(fileLines);
        }
        catch(Exception e){
            throw new BlockedIpStoreException(LOG_ENTRY_STORE_ERR_LOADING_LOG_FILE, e);
        }
    }

    private Stream<String> covertFileContentsToStream(String filePath) throws Exception {
        return Files.lines(Paths.get(filePath));
    }

    private void saveLinesAsAccessLogEntries(Stream<String> lines) {
        lines.map(toAccessLogEntry).forEach(saveEntry());
    }

    private Persister saveEntry() {
        return speedmentStoreComponents.logEntryManager.persister();
    }

    private final Function<String, AccessLogEntry> toAccessLogEntry = (line) -> {
        String[] entry = line.split(DELIMITER);
        return new AccessLogEntryImpl()
                .setDate(LocalDateTime.parse(entry[0], ParserUtils.LOG_FILE_DATE_FORMATTER))
                .setIpAddress(IpAddressConverter.fromStringToLong(entry[1]))
                .setRequest(entry[2])
                .setStatus(Integer.parseInt(entry[3]))
                .setUserAgent(entry[4]);
    };

    @Override
    public List<Long> findIpsToBlock(SearchCriteria blockingCriteria) {
        LocalDateTime startDate = blockingCriteria.startDate();
        LocalDateTime endDate = getHourlyDefaultedEndDate(startDate);
        if (blockingCriteria.duration() == DAILY) {
            endDate = startDate.plusDays(1);
        }

        List<Long> iPsToBlock;
        try {
            iPsToBlock = speedmentStoreComponents.logEntryManager.stream()
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
            throw new BlockedIpStoreException("Failure in SpeedmentBlockedIpStore::findIpsToBlock. " +
                    "Error finding blocked IPs in database table.", e);
        }
        return iPsToBlock;
    }



    private LocalDateTime getHourlyDefaultedEndDate(LocalDateTime startDate) {
        return deriveEndDateFromStartDate(startDate);
    }

    private LocalDateTime deriveEndDateFromStartDate(LocalDateTime startDate) {
        return defaultToHourlyDuration(startDate);
    }

    private LocalDateTime defaultToHourlyDuration(LocalDateTime startDate) {
        return startDate.plusHours(1);
    }

    @Override
    public void saveIpsToBlock(SearchCriteria blockingCriteria, List<Long> iPsToBlock) {
        String comment = getBlockingReasonComment(blockingCriteria);
        try {
            iPsToBlock.stream()
                    .map(ip ->
                         new BlockedIpImpl()
                        .setIpAddress(ip)
                        .setComment(comment))
                    .forEach(speedmentStoreComponents.blockedIpManager.persister());
        } catch (SpeedmentException e) {
            throw new BlockedIpStoreException("Failure in SpeedmentBlockedIpStore::saveIpsToBlock. " +
                    "Error writing blocked IPs to database.", e);
        }
    }

    private String getBlockingReasonComment(SearchCriteria blockingCriteria) {
        return new StringBuilder("IP exceeded max of [")
                .append(blockingCriteria.threshold()).append("] ")
                .append("allowed requests in more than ")
                .append(blockingCriteria.duration() == Duration.HOURLY ? "an" : "a").append(" [")
                .append(blockingCriteria.duration().toString()).append("] ")
                .append("period starting at [")
                .append(blockingCriteria.startDate().toString()).append("].")
                .toString();
    }

    @Override
    public void shutdownStore() {
        speedmentStoreComponents.appDb.close();
    }
}
