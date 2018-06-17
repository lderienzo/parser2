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

public class AccessLogReader implements LogReader {

    private static final String DELIMITER = "\\|";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private AccessLogEntryManager logEntryManager;

    public AccessLogReader(AccessLogEntryManager logEntryManager) {
        this.logEntryManager = logEntryManager;
    }

    @Override
    public void read(String path) {
        try (Stream<String> lines = Files.lines(Paths.get(path))) {
            lines.map(mapToAccessLogEntry).forEach(logEntryManager.persister());
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
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

        Map<Long, Long> blockedIps = logEntryManager.stream()
                .filter(AccessLogEntry.DATE.between(startDate, endDate, START_INCLUSIVE_END_INCLUSIVE))
                .collect(
                        Collectors.groupingBy(AccessLogEntry::getIpAddress,// classification function, instance of the Function<T,R> T=AccessLogEntry?, R=Long
                                Collectors.counting()
                        )
                )
                .entrySet().stream()
                .filter(entry -> entry.getValue() >= threshold)
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));

        return (blockedIps == null ? new HashMap<>() : blockedIps);
    }

}
