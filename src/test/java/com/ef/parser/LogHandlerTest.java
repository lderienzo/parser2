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
import com.ef.parser.db.parser.parser.blocked_ip.BlockedIpManager;
import com.speedment.runtime.core.ApplicationBuilder;

public final class LogHandlerTest {
    private AccessLogEntryManager logEntryManager;
    private BlockedIpManager blockedIpManager;
    private LogHandler logHandler;
    private LocalDateTime startDate = ParserTestUtils.stringToLocalDateTime("2017-01-01 20:00:00");
    private String duration = ParserTestUtils.HOURLY_DURATION;
    private int threshold = ParserTestUtils.THRESHOLD_100;

    @Before
    public void setUp() {
        ParserApplication db = new ParserApplicationBuilder() // initialize db connection...
                .withPassword(DB_PWD)
                .withLogging(ApplicationBuilder.LogType.PERSIST)
                .withLogging(ApplicationBuilder.LogType.STREAM)
                .build();
        logEntryManager = db.getOrThrow(AccessLogEntryManager.class);
        blockedIpManager = db.getOrThrow(BlockedIpManager.class);

        logHandler = new AccessLogHandler(logEntryManager, blockedIpManager);
    }

    @Test
    public void testReadOfLogIntoDatabase() { // TODO: figure out how to do this
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(TEST_ACCESS_LOG_FILE).getFile());
        logHandler.read(file.getAbsolutePath());
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
        logHandler.printBlockedIps(blockedIps);
        // TODO: figure out how to test
    }

    @Test
    public void testSaveBlockedIps() {
        Map<Long,Long> blockedIps = getBlockedIps();
        logHandler.saveBlockedIps(blockedIps, startDate, duration, threshold);
        // TODO: figure out how to test
    }


    private Map<Long, Long> getBlockedIps() {
        return logHandler.getBlockedIps(startDate, duration, threshold);
    }
}
