package com.ef.parser;

import static com.ef.parser.ParserUtils.DAILY_DURATION;
import static com.speedment.runtime.field.predicate.Inclusion.START_INCLUSIVE_END_INCLUSIVE;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ef.parser.db.parser.parser.access_log_entry.AccessLogEntry;
import com.ef.parser.db.parser.parser.access_log_entry.AccessLogEntryImpl;
import com.ef.parser.db.parser.parser.access_log_entry.AccessLogEntryManager;
import com.ef.parser.db.parser.parser.blocked_ip.BlockedIp;
import com.ef.parser.db.parser.parser.blocked_ip.BlockedIpImpl;
import com.ef.parser.db.parser.parser.blocked_ip.BlockedIpManager;
import com.speedment.runtime.core.exception.SpeedmentException;

public class AccessLogReader implements LogReader {

    private static final String DELIMITER = "\\|";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private AccessLogEntryManager logEntryManager;
    private BlockedIpManager blockedIpManager;

    public AccessLogReader(AccessLogEntryManager logEntryManager, BlockedIpManager blockedIpManager) {
        this.logEntryManager = logEntryManager;
        this.blockedIpManager = blockedIpManager;
    }

    @Override
    public void read(String path) {
        try (Stream<String> lines = Files.lines(Paths.get(path))) {
            lines.map(mapToAccessLogEntry).forEach(logEntryManager.persister());
        }
        catch (IOException|SpeedmentException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private Function<String, AccessLogEntry> mapToAccessLogEntry = (line) -> {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        String[] entry = line.split(DELIMITER);
        return new AccessLogEntryImpl()
                .setDate(LocalDateTime.parse(entry[0], formatter))
                .setIpAddress(IpAddressConverter.toLong(entry[1]))
                .setRequest(entry[2])
                .setStatus(Integer.parseInt(entry[3]))
                .setUserAgent(entry[4]);
    };

    @Override
    public Map<Long, Long> getBlockedIps(LocalDateTime startDate, String duration, int threshold) {
        LocalDateTime endDate = startDate.plusHours(1);  // default to hourly duration
        if (DAILY_DURATION.equals(duration)) {
            endDate = startDate.plusDays(1);
        }

        Map<Long, Long> blockedIps = new HashMap<>();
        try {
            blockedIps = logEntryManager.stream()
                .filter(AccessLogEntry.DATE.between(startDate, endDate, START_INCLUSIVE_END_INCLUSIVE))
                .collect(
                        Collectors.groupingBy(AccessLogEntry::getIpAddress,// classification function, instance of the Function<T,R> T=AccessLogEntry?, R=Long
                                Collectors.counting()
                        )
                )
                .entrySet().stream()
                .filter(entry -> entry.getValue() >= threshold)
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
        } catch (SpeedmentException ex) {
            System.err.println(ex.getMessage());
        }

        return (blockedIps == null ? new HashMap<>() : blockedIps);
    }

    @Override
    public void printBlockedIps(Map<Long, Long> blockedIps) {
        if (blockedIps.size() > 0) {
            System.out.println("Blocked IPs:");
            blockedIps.keySet().stream().sorted()
                    .forEach(key -> System.out.println(IpAddressConverter.toIp(key)));
        }
        else {
            System.out.println("'blockedIps' map is empty. Nothing to save. Aborting...");
        }

    }

    @Override
    public void saveBlockedIps(Map<Long, Long> blockedIps, LocalDateTime startDate, String duration, int threshold) {
        if (blockedIps.size() > 0) {
            try {
                blockedIps.entrySet().stream()
                    .map(entry -> new BlockedIpImpl()
                            .setIpAddress(entry.getKey())
                            .setComment("Request threshold of ["+threshold+"] exceeded by ["+ (entry.getValue()-threshold) +"] " +
                                    "within ["+duration+"] duration starting on ["+startDate.toString()+"]'."))
                    .forEach(blockedIpManager.persister());
            } catch (SpeedmentException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
}
