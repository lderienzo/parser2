package com.ef.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ef.parser.db.IpAddressConverter;
import com.ef.parser.db.ParserApplication;
import com.ef.parser.db.ParserApplicationBuilder;
import com.ef.parser.db.parser.parser.access_log_entry.AccessLogEntry;
import com.ef.parser.db.parser.parser.access_log_entry.AccessLogEntryImpl;
import com.ef.parser.db.parser.parser.access_log_entry.AccessLogEntryManager;
import com.speedment.runtime.core.ApplicationBuilder;
import com.speedment.runtime.core.exception.SpeedmentException;

public class AccessLogReader implements LogReader {

    private static final String DELIMITER = "\\|";

    // read file contents into Stream/List of LogEntry objects and return

    public void read(String path) {

        if (path == null) {
            throw new IllegalArgumentException("Null/Empty Log File Path");
        }

        // initialize db connection...
        ParserApplication app = new ParserApplicationBuilder()
                .withPassword("password").withLogging(ApplicationBuilder.LogType.PERSIST).build();

        AccessLogEntryManager accessLogEntry = app.getOrThrow(AccessLogEntryManager.class);

        try (Stream<String> lines = Files.lines(Paths.get(path))) {

            lines.map(mapToAccessLogEntry)
//                    .forEach(entry -> System.out.println(entry.toString()));
                    .forEach(accessLogEntry.persister());
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Function<String, AccessLogEntry> mapToAccessLogEntry = (line) -> {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        String[] entry =  line.split(DELIMITER);
        return new AccessLogEntryImpl()
                    .setDate(LocalDateTime.parse(entry[0], formatter))
                    .setIpAddress(IpAddressConverter.toLong(entry[1]))
                    .setRequest(entry[2])
                    .setStatus(Integer.parseInt(entry[3]))
                    .setUserAgent(entry[4]);
    };

}
