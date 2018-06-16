package com.ef.parser;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;
import java.util.stream.Stream;

import com.ef.parser.db.IpAddressConverter;
import com.ef.parser.db.ParserApplication;
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

    /**
     * Reads in log file and stores each row in the database.
     * @param path
     */
    public void read(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Null/Empty Log File Path");
        }

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

}
