package com.ef.parser;

import static com.ef.parser.ParserTestUtils.TEST_ACCESS_LOG_FILE;
import static com.ef.parser.ParserUtils.DB_PWD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.ef.parser.db.ParserApplication;
import com.ef.parser.db.ParserApplicationBuilder;
import com.ef.parser.db.parser.parser.access_log_entry.AccessLogEntryManager;
import com.speedment.runtime.core.ApplicationBuilder;

public final class LogReaderTest {
    private ParserApplication db;
    private AccessLogEntryManager logEntryManager;
    private LogReader logReader;

    @Before
    public void setUp() {
        ParserApplication db = new ParserApplicationBuilder() // initialize db connection...
                .withPassword(DB_PWD)
                .withLogging(ApplicationBuilder.LogType.PERSIST)
                .withLogging(ApplicationBuilder.LogType.STREAM)
                .build();
        logEntryManager = db.getOrThrow(AccessLogEntryManager.class);
        logReader = new AccessLogReader(logEntryManager);
    }

    @Test
    public void testReadOfLogIntoDatabase() { // TODO: figure out how to do this
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(TEST_ACCESS_LOG_FILE).getFile());
        logReader.read(file.getAbsolutePath());
    }

    @Test
    public void testGetBlockedIps() {
        Map<Long,Long> blockedIps = getBlockedIps();
        assertNotNull(blockedIps);
        assertEquals(6, blockedIps.size());
    }

    @Test
    public void testPrintBlockedIps() {
        Map<Long,Long> blockedIps = getBlockedIps();
        logReader.printBlockedIps(blockedIps);
    }


    private Map<Long, Long> getBlockedIps() {
        LocalDateTime startDate = ParserTestUtils.stringToLocalDateTime("2017-01-01 20:00:00");
        String duration = ParserTestUtils.HOURLY_DURATION;
        int threshold = ParserTestUtils.THRESHOLD_100;

        return logReader.getBlockedIps(startDate, duration, threshold);
    }
}
