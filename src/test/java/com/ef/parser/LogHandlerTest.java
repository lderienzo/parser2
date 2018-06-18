package com.ef.parser;


import static com.ef.parser.ParserTestUtils.DAILY_TEST_ACCESS_LOG;
import static com.ef.parser.ParserTestUtils.DAILY_TEST_IP;
import static com.ef.parser.ParserTestUtils.DAILY_TEST_IP_LONG;
import static com.ef.parser.ParserTestUtils.DAILY_TEST_START_DATE;
import static com.ef.parser.ParserTestUtils.HOURLY_TEST_ACCESS_LOG;
import static com.ef.parser.ParserTestUtils.HOURLY_TEST_IP;
import static com.ef.parser.ParserTestUtils.HOURLY_TEST_IP_LONG;
import static com.ef.parser.ParserTestUtils.HOURLY_TEST_START_DATE;
import static com.ef.parser.ParserTestUtils.SHORT_TEST_ACCESS_LOG_FILE;
import static com.ef.ParserUtils.BLOCKED_IPS_MESSAGE_HEADER;
import static com.ef.ParserUtils.DB_PWD;
import static com.ef.ParserUtils.NO_BLOCKED_IPS_TO_REPORT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.ef.AccessLogHandler;
import com.ef.Duration;
import com.ef.IpAddressConverter;
import com.ef.LogHandler;
import com.ef.db.ParserApplication;
import com.ef.db.ParserApplicationBuilder;
import com.ef.db.parser.parser.access_log_entry.AccessLogEntryManager;
import com.ef.db.parser.parser.blocked_ip.BlockedIpImpl;
import com.ef.db.parser.parser.blocked_ip.BlockedIpManager;
import com.speedment.runtime.core.ApplicationBuilder;

public final class LogHandlerTest {
    private static AccessLogEntryManager logEntryManager;
    private static BlockedIpManager blockedIpManager;
    private static LogHandler logHandler;

    @BeforeClass
    public static void setUp() {
        ParserApplication db = new ParserApplicationBuilder() // initialize db connection...
                .withPassword(DB_PWD)
                .withLogging(ApplicationBuilder.LogType.PERSIST)
                .withLogging(ApplicationBuilder.LogType.STREAM)
                .withLogging(ApplicationBuilder.LogType.REMOVE)
                .build();
        logEntryManager = db.getOrThrow(AccessLogEntryManager.class);
        blockedIpManager = db.getOrThrow(BlockedIpManager.class);

        logHandler = new AccessLogHandler(logEntryManager, blockedIpManager);
    }

    @After
    public void clearDB() {
        clearDbTables();
    }

    @Test
    public void testReadOfLogIntoDatabase() {
        readLogFileIntoDb(SHORT_TEST_ACCESS_LOG_FILE);
        long count = countLogEntryRows().longValue();
        assertTrue(count == 377);

        clearDbTables();
    }

    @Test
    public void testGetBlockedIps_hourly() {
        readLogFileIntoDb(HOURLY_TEST_ACCESS_LOG);

        LocalDateTime startDate = ParserTestUtils.stringToLocalDateTime(HOURLY_TEST_START_DATE);
        Duration duration = Duration.HOURLY;
        int threshold = ParserTestUtils.THRESHOLD_200;
        Map<Long,Long> blockedIps = getBlockedIps(startDate, duration, threshold);

        assertNotNull(blockedIps);
        assertEquals(2, blockedIps.size());
        assertTrue(blockedIps.containsKey(HOURLY_TEST_IP_LONG));

        String strIp = IpAddressConverter.toIp(HOURLY_TEST_IP_LONG);
        assertEquals(HOURLY_TEST_IP, strIp);
    }

    @Test
    public void testSaveBlockedIps_hourly() {
        readLogFileIntoDb(HOURLY_TEST_ACCESS_LOG);

        LocalDateTime startDate = ParserTestUtils.stringToLocalDateTime(HOURLY_TEST_START_DATE);
        Duration duration = Duration.HOURLY;
        int threshold = ParserTestUtils.THRESHOLD_200;
        Map<Long,Long> blockedIps = getBlockedIps(startDate, duration, threshold);

        assertNotNull(blockedIps);
        assertEquals(2, blockedIps.size());
        assertTrue(blockedIps.containsKey(HOURLY_TEST_IP_LONG));

        String strIp = IpAddressConverter.toIp(HOURLY_TEST_IP_LONG);
        assertEquals(HOURLY_TEST_IP, strIp);

        logHandler.saveBlockedIps(blockedIps, startDate, duration, threshold);

        Long count = countBlockedIpRows();
        assertTrue(count == 2);

        Set<Long> blockedIpSet = blockedIpManager.stream()
                .filter(BlockedIpImpl.IP_ADDRESS.equal(HOURLY_TEST_IP_LONG))
                .mapToLong(BlockedIpImpl.IP_ADDRESS)
                .boxed().collect(Collectors.toSet());
        assertTrue(blockedIpSet.size() == 1);
        assertTrue(blockedIpSet.contains(HOURLY_TEST_IP_LONG));

        strIp = IpAddressConverter.toIp(HOURLY_TEST_IP_LONG);
        assertEquals(HOURLY_TEST_IP, strIp);
    }

    @Test
    public void testGetBlockedIps_daily() {         // this is the whole log file
        readLogFileIntoDb(DAILY_TEST_ACCESS_LOG);

        LocalDateTime startDate = ParserTestUtils.stringToLocalDateTime(DAILY_TEST_START_DATE);
        Duration duration = Duration.DAILY;
        int threshold = ParserTestUtils.THRESHOLD_500;
        Map<Long,Long> blockedIps = getBlockedIps(startDate, duration, threshold);

        assertNotNull(blockedIps);
        assertEquals(15, blockedIps.size());
        assertTrue(blockedIps.containsKey(DAILY_TEST_IP_LONG));

        String strIp = IpAddressConverter.toIp(DAILY_TEST_IP_LONG);
        assertEquals(DAILY_TEST_IP, strIp);
    }

    @Test
    public void testSaveBlockedIps_daily() {           // this is the whole log file
        readLogFileIntoDb(DAILY_TEST_ACCESS_LOG);

        LocalDateTime startDate = ParserTestUtils.stringToLocalDateTime(DAILY_TEST_START_DATE);
        Duration duration = Duration.DAILY;
        int threshold = ParserTestUtils.THRESHOLD_500;
        Map<Long,Long> blockedIps = getBlockedIps(startDate, duration, threshold);

        assertNotNull(blockedIps);
        assertEquals(15, blockedIps.size());
        assertTrue(blockedIps.containsKey(DAILY_TEST_IP_LONG));

        String strIp = IpAddressConverter.toIp(DAILY_TEST_IP_LONG);
        assertEquals(DAILY_TEST_IP, strIp);

        logHandler.saveBlockedIps(blockedIps, startDate, duration, threshold);

        Long count = countBlockedIpRows();
        assertTrue(count == 15);

        Set<Long> blockedIpSet = blockedIpManager.stream()
                .filter(BlockedIpImpl.IP_ADDRESS.equal(DAILY_TEST_IP_LONG))
                .mapToLong(BlockedIpImpl.IP_ADDRESS)
                .boxed().collect(Collectors.toSet());
        assertTrue(blockedIpSet.size() == 1);
        assertTrue(blockedIpSet.contains(DAILY_TEST_IP_LONG));

        strIp = IpAddressConverter.toIp(DAILY_TEST_IP_LONG);
        assertEquals(DAILY_TEST_IP, strIp);
    }

    @Test
    public void testGetBlockedIpsMessage_success() {
        readLogFileIntoDb(HOURLY_TEST_ACCESS_LOG);

        LocalDateTime startDate = ParserTestUtils.stringToLocalDateTime(HOURLY_TEST_START_DATE);
        Duration duration = Duration.HOURLY;
        int threshold = ParserTestUtils.THRESHOLD_200;
        Map<Long,Long> blockedIps = getBlockedIps(startDate, duration, threshold);

        assertNotNull(blockedIps);
        assertEquals(2, blockedIps.size());
        assertTrue(blockedIps.containsKey(HOURLY_TEST_IP_LONG));

        String strIp = IpAddressConverter.toIp(HOURLY_TEST_IP_LONG);
        assertEquals(HOURLY_TEST_IP, strIp);

        String message = logHandler.getBlockedIpsMessage(blockedIps);
        assertTrue(message.contains(BLOCKED_IPS_MESSAGE_HEADER));
    }

    @Test
    public void testGetBlockedIpsMessage_failure() {
        String message = logHandler.getBlockedIpsMessage(null);
        assertTrue(message.contains(NO_BLOCKED_IPS_TO_REPORT));
    }

    private void readLogFileIntoDb(String filePath) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(filePath).getFile());
        logHandler.read(file.getAbsolutePath());
    }

    private Map<Long, Long> getBlockedIps(LocalDateTime startDate, Duration duration, int threshold) {
        return logHandler.getBlockedIps(startDate, duration, threshold);
    }

    private Long countLogEntryRows() {
        Long count = logEntryManager.stream().count();
        return count;
    }

    private Long countBlockedIpRows() {
        Long count = blockedIpManager.stream().count();
        return count;
    }

    private void clearDbTables() {
        logEntryManager.stream().forEach(logEntryManager.remover());
        blockedIpManager.stream().forEach(blockedIpManager.remover());
        Long logEntryCount = countLogEntryRows();
        Long blockedIpCount = countBlockedIpRows();
        assertTrue(logEntryCount == 0);
        assertTrue(blockedIpCount == 0);
    }
}
