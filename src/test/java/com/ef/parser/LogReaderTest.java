package com.ef.parser;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.ef.parser.db.parser.parser.access_log_entry.AccessLogEntryManager;

public final class LogReaderTest {
    private AccessLogEntryManager logEntryManager;
    private LogReader logReader;

    @Before
    public void setUp() {
        logReader = new AccessLogReader(logEntryManager);
    }

    @Test
    public void testLogReader() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("test_access.log").getFile());
        logReader.read(file.getAbsolutePath());
    }
}
