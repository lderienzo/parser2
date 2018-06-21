package com.ef.loghandler;

import static com.ef.enums.Duration.DAILY;
import static com.ef.utils.ParserUtils.BLOCKED_IPS_MESSAGE_HEADER;
import static com.ef.utils.ParserUtils.NO_BLOCKED_IPS_TO_REPORT;
import static com.ef.utils.ParserUtils.NO_BLOCKED_IPS_TO_SAVE;
import static com.ef.utils.ParserUtils.STATUS_MESSAGE_FAILURE;
import static com.ef.utils.ParserUtils.STATUS_MESSAGE_SUCCESS;
import static com.speedment.runtime.field.predicate.Inclusion.START_INCLUSIVE_END_INCLUSIVE;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
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
import com.ef.utils.IpAddressConverter;
import com.ef.utils.ParserUtils;
import com.speedment.runtime.core.exception.SpeedmentException;

public class AccessLogHandler implements LogHandler {
    private static final String DELIMITER = "\\|";
    private AccessLogEntryManager logEntryManager;
    private BlockedIpManager blockedIpManager;

    public AccessLogHandler(AccessLogEntryManager logEntryManager, BlockedIpManager blockedIpManager) {
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

    private Function<String,AccessLogEntry> mapToAccessLogEntry = (line) -> {
        String[] entry = line.split(DELIMITER);
        return new AccessLogEntryImpl()
                .setDate(LocalDateTime.parse(entry[0], ParserUtils.LOG_FILE_DATE_FORMATTER))
                .setIpAddress(IpAddressConverter.toLong(entry[1]))
                .setRequest(entry[2])
                .setStatus(Integer.parseInt(entry[3]))
                .setUserAgent(entry[4]);
    };

    @Override
    public Map<Long,Long> getBlockedIps(LocalDateTime startDate, Duration duration, int threshold) {
        LocalDateTime endDate = startDate.plusHours(1);  // default to hourly duration
        if (duration == DAILY) {
            endDate = startDate.plusDays(1);
        }
        Map<Long,Long> blockedIps = new HashMap<>();
        try {
            blockedIps = logEntryManager.stream()
                .filter(AccessLogEntry.DATE.between(startDate, endDate, START_INCLUSIVE_END_INCLUSIVE))
                .collect(
                        Collectors.groupingBy(AccessLogEntry::getIpAddress,
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
    public String getBlockedIpsMessage(Map<Long,Long> blockedIps) {
        StringBuilder blockedIpsMessage = new StringBuilder();
        if (blockedIps!= null && blockedIps.size() > 0) {
            blockedIpsMessage.append(BLOCKED_IPS_MESSAGE_HEADER).append("\n");
            blockedIps.keySet().stream().sorted()
                    .forEach(key -> blockedIpsMessage.append(IpAddressConverter.toIp(key)).append("\n"));
        }
        else {
            blockedIpsMessage.append(NO_BLOCKED_IPS_TO_REPORT);
        }
        return blockedIpsMessage.toString();
    }

    @Override
    public String saveBlockedIps(Map<Long,Long> blockedIps, LocalDateTime startDate, Duration duration, int threshold) {
        String statusMessage = STATUS_MESSAGE_SUCCESS;
        if (blockedIps.size() > 0) {
            try {
                blockedIps.entrySet().stream()
                    .map(entry -> new BlockedIpImpl()
                            .setIpAddress(entry.getKey())
                            .setComment("Request threshold of ["+threshold+"] exceeded by ["+ (entry.getValue()-threshold) +"] " +
                                    "within ["+duration.toString().toLowerCase()+"] duration starting on ["+startDate.toString()+"]'."))
                    .forEach(blockedIpManager.persister());
            } catch (SpeedmentException ex) {
                statusMessage = STATUS_MESSAGE_FAILURE;
                System.err.println(ex.getMessage());
            }
        }
        else {
            statusMessage = NO_BLOCKED_IPS_TO_SAVE;
        }
        return statusMessage;
    }
}
