package com.ef.loghandler;


import static com.ef.utils.ParserTestUtils.BOGUS_TEST_LOG_FILE_PATH;
import static com.ef.utils.ParserTestUtils.DAILY_TEST_ACCESS_LOG;
import static com.ef.utils.ParserTestUtils.DAILY_TEST_IP;
import static com.ef.utils.ParserTestUtils.DAILY_TEST_IP_LONG;
import static com.ef.utils.ParserTestUtils.DAILY_TEST_START_DATE;
import static com.ef.utils.ParserTestUtils.HOURLY_TEST_ACCESS_LOG;
import static com.ef.utils.ParserTestUtils.HOURLY_TEST_IP;
import static com.ef.utils.ParserTestUtils.HOURLY_TEST_IP_LONG;
import static com.ef.utils.ParserTestUtils.HOURLY_TEST_START_DATE;
import static com.ef.utils.ParserTestUtils.INVALID_LINE_FORMAT_LOG_FILE;
import static com.ef.utils.ParserTestUtils.SHORT_TEST_ACCESS_LOG_FILE;
import static com.ef.utils.ParserUtils.BLOCKED_IPS_MESSAGE_HEADER;
import static com.ef.utils.ParserUtils.DB_PWD;
import static com.ef.utils.ParserUtils.NO_BLOCKED_IPS_TO_REPORT;
import static java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ef.enums.Duration;
import com.ef.utils.IpAddressConverter;
import com.ef.db.ParserApplication;
import com.ef.db.ParserApplicationBuilder;
import com.ef.db.parser.parser.access_log_entry.AccessLogEntryManager;
import com.ef.db.parser.parser.blocked_ip.BlockedIpImpl;
import com.ef.db.parser.parser.blocked_ip.BlockedIpManager;
import com.ef.utils.ParserTestUtils;
import com.speedment.runtime.core.ApplicationBuilder;

public final class LogHandlerTest {
    private static ParserApplication db;
    private static AccessLogEntryManager logEntryManager;
    private static BlockedIpManager blockedIpManager;
    private static LogHandler logHandler;

    @BeforeClass
    public static void setUp() {
        db = new ParserApplicationBuilder() // initialize db connection...
                .withPassword(DB_PWD)
                .withLogging(ApplicationBuilder.LogType.PERSIST)
                .withLogging(ApplicationBuilder.LogType.STREAM)
                .withLogging(ApplicationBuilder.LogType.REMOVE)
                .build();
        logEntryManager = db.getOrThrow(AccessLogEntryManager.class);
        blockedIpManager = db.getOrThrow(BlockedIpManager.class);

        logHandler = new AccessLogHandler(logEntryManager, blockedIpManager);
    }

    @AfterClass
    public static void tearDown() {
        db.close();
    }

    @After
    public void clearDB() {
        clearDbTables();
    }

    @Test
    public void testSave_success() {
        readLogFileIntoDb(SHORT_TEST_ACCESS_LOG_FILE);
        long count = countLogEntryRows().longValue();
        assertTrue(count == 377);

        clearDbTables();
    }

    @Test(expected = LogHandlerException.class)
    public void testSave_failure_reading_file() {
        logHandler.save(BOGUS_TEST_LOG_FILE_PATH);
    }

    @Test(expected = LogHandlerException.class)
    public void testSave_failure_db_error_invalid_log_file_format() {
        readLogFileIntoDb(INVALID_LINE_FORMAT_LOG_FILE);
    }

    @Test
    public void testSaveBlockedIps_hourly_success() {
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

        int status = logHandler.saveBlockedIps(blockedIps, startDate, duration, threshold);
        assertEquals(1, status);

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
    public void testSaveBlockedIps_daily_success() {           // this is the whole log file
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

        int status = logHandler.saveBlockedIps(blockedIps, startDate, duration, threshold);
        assertEquals(1, status);

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
    public void testSaveBlockedIps_no_blockedIps() {
        LocalDateTime startDate = ParserTestUtils.stringToLocalDateTime(HOURLY_TEST_START_DATE);
        Duration duration = Duration.HOURLY;
        int threshold = ParserTestUtils.THRESHOLD_500;
        Map<Long,Long> blockedIps = new HashMap<>();

        assertNotNull(blockedIps);
        assertEquals(0, blockedIps.size());

        int status = logHandler.saveBlockedIps(blockedIps, startDate, duration, threshold);
        assertEquals(0, status);
    }

    @Test(expected = LogHandlerException.class)
    public void testSaveBlockedIps_failure() {
        LocalDateTime startDate = ParserTestUtils.stringToLocalDateTime(HOURLY_TEST_START_DATE);
        Duration duration = Duration.HOURLY;
        int threshold = ParserTestUtils.THRESHOLD_500;
        Map<Long,Long> blockedIps = new HashMap<>();
        blockedIps.put(-2147483648L,-2147483648L);
        logHandler.saveBlockedIps(blockedIps, startDate, duration, threshold);
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

        String blockedIpsMessage = logHandler.getMessage(blockedIps);
        assertTrue(blockedIpsMessage.contains(BLOCKED_IPS_MESSAGE_HEADER));
    }

    @Test
    public void testGetBlockedIpsMessage_failure() {
        String blockedIpsMessage = logHandler.getMessage(null);
        assertTrue(blockedIpsMessage.contains(NO_BLOCKED_IPS_TO_REPORT));
    }

    private void readLogFileIntoDb(String filePath) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(filePath).getFile());
        logHandler.save(file.getAbsolutePath());
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
