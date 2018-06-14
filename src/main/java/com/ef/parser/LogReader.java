package com.ef.parser;

import java.util.List;

public interface LogReader {
    List<LogEntry> read(String path);
}
