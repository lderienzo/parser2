package com.ef.parser;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class AccessLogReader implements LogReader {

    // read file contents into Stream/List of LogEntry objects and return

    public List<LogEntry> read(String path) {

        if (path == null) {
            throw new IllegalArgumentException("Null/Empty Log File Path");
        }

        try (Stream<String> lines = Files.lines(Paths.get(path))) {
//            lines.map(line -> line.split("|")); //map method returns an array of String (String[]) for each line in the file.
            lines.forEach(System.out::println);
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return new ArrayList<>();
    }
}
