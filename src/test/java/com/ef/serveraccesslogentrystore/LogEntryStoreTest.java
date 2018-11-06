/*
 * Created by Luke DeRienzo on 10/30/18 2:58 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 10/30/18 2:58 PM
 */

package com.ef.serveraccesslogentrystore;

import static com.ef.enums.Duration.DAILY;
import static com.ef.enums.Duration.HOURLY;
import static com.ef.utils.ParserTestUtils.BOGUS_TEST_LOG_FILE_PATH;
import static com.ef.utils.ParserTestUtils.INVALID_LINE_FORMAT_LOG_FILE;
import static com.ef.utils.ParserTestUtils.NUM_OF_TEST_ACCESS_LOG_FILE_ENTRIES;
import static com.ef.utils.ParserTestUtils.TEST_ACCESS_LOG_FILE;
import static com.ef.utils.ParserTestUtils.TEST_START_DATE;
import static com.ef.utils.ParserTestUtils.THRESHOLD_200;
import static com.ef.utils.ParserTestUtils.THRESHOLD_TO_FIND_MULTIPLE_IPS_FOR_DAY;
import static com.ef.utils.ParserTestUtils.THRESHOLD_TO_FIND_MULTIPLE_IPS_FOR_HOUR;
import static com.ef.utils.ParserTestUtils.THRESHOLD_TO_FIND_ONE_IP_FOR_DAY;
import static com.ef.utils.ParserTestUtils.THRESHOLD_TO_FIND_ONE_IP_FOR_HOUR;
import static com.ef.utils.ParserUtils.DB_PWD;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ef.arguments.Args;
import com.ef.db.ParserApplication;
import com.ef.db.ParserApplicationBuilder;
import com.ef.db.parser.parser.access_log_entry.AccessLogEntryManager;
import com.ef.db.parser.parser.blocked_ip.BlockedIp;
import com.ef.db.parser.parser.blocked_ip.BlockedIpManager;
import com.ef.enums.Duration;
import com.ef.loghandler.LogHandlerException;
import com.speedment.runtime.core.ApplicationBuilder;
import com.speedment.runtime.core.exception.SpeedmentException;
import com.speedment.runtime.core.manager.Manager;


public class LogEntryStoreTest {
    private static ParserApplication db;
    private static AccessLogEntryManager logEntryManager;
    private static BlockedIpManager blockedIpManager;
    private static LogEntryStore logEntryStore;
    private SearchCriteria blockingCriteria;
    private SearchCriteria blockingCriteriaForComment;
    private final List<Long> expectedSingleBlockedIpForDay = Collections.singletonList(3232278978L);
    private final List<Long> expectedSingleBlockedIpForHour = Collections.singletonList(3232278978L);
    private final List<Long> expectedMultipleBlockedIpsForDay =
        Stream.of(3232243984L, 3232245325L, 3232246155L, 3232248781L, 3232261768L,
                  3232268735L, 3232269563L, 3232272305L, 3232277240L,
                  3232278978L, 3232281022L, 3232286673L, 3232287599L,
                  3232288397L, 3232291594L, 3232295506L).collect(
        Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    private final List<Long> expectedMultipleBlockedIpsForHour =
        Stream.of(3232245325L, 3232248781L, 3232272305L,
                  3232278978L, 3232287599L, 3232295506L).collect(
            Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

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
        logEntryStore = new LogEntryStore(logEntryManager, blockedIpManager);
    }

    @AfterClass
    public static void tearDown() {
        db.close();
    }

    @Before
    public void loadLogFileIntoDb() {
        testLoadFile(TEST_ACCESS_LOG_FILE);
    }

    public void testLoadFile(String path) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(path).getFile());
        logEntryStore.loadFile(file.getAbsolutePath());
        checkLogFileWasLoaded();
    }

    private void checkLogFileWasLoaded() {
        long numberOfLogEntries = countNumberOfEntries(logEntryManager);
        assertTrue(numberOfLogEntries == NUM_OF_TEST_ACCESS_LOG_FILE_ENTRIES);
    }

    private long countNumberOfEntries(Manager manager) {
        return manager.stream().count();
    }

    @Test(expected = LogHandlerException.class)
    public void testLoadFile_badFilePath() {
        logEntryStore.loadFile(BOGUS_TEST_LOG_FILE_PATH);
    }

    @Test(expected = LogHandlerException.class)
    public void testLoadFile_invalidFileFormat() {
        testLoadFile(INVALID_LINE_FORMAT_LOG_FILE);
    }

    @After
    public void clearDb() {
        logEntryManager.stream().forEach(logEntryManager.remover());
        blockedIpManager.stream().forEach(blockedIpManager.remover());
        checkDbWasCleared();
    }

    private void checkDbWasCleared() {
        long numberOfLogEntries = countNumberOfEntries(logEntryManager);
        long numberOfBlockedIpEntries = countNumberOfEntries(blockedIpManager);
        assertTrue(numberOfLogEntries == 0);
        assertTrue(numberOfBlockedIpEntries == 0);
    }

    @Test
    public void testFindIpsToBlock_noneFound() {
        testNotFindingAnyIpsToBlock();
    }

    private List<Long> testNotFindingAnyIpsToBlock() {
        blockingCriteria =
            getSearchCriteriaForFindingIpsToBlock(
                withArgsForFindingNothing(HOURLY, THRESHOLD_200));
        List<Long> iPsToBlock =
            testFindingIpsToBlock(blockingCriteria, Collections.EMPTY_LIST);
        return iPsToBlock;
    }

    private String[] withArgsForFindingNothing(Duration duration, int threshold) {
        return getArgs(duration, threshold);
    }

    private String[] getArgs(Duration duration, int threshold) {
        String[] commandLineArgs = {
            new StringBuilder("--").append(Args.ArgName.START_DATE)
                    .append("=").append(TEST_START_DATE).toString(),
            new StringBuilder("--").append(Args.ArgName.DURATION)
                    .append("=").append(duration).toString(),
            new StringBuilder("--").append(Args.ArgName.THRESHOLD)
                    .append("=").append(threshold).toString()
        };
        return commandLineArgs;
    }

    private List<Long> testFindingIpsToBlock(SearchCriteria searchCriteria,
                                             List<Long> expectedBlockedIps) {
        List<Long> iPsToBlock = logEntryStore.findIpsToBlock(searchCriteria);
        checkIfFound(iPsToBlock, expectedBlockedIps);
        return iPsToBlock;
    }

    private SearchCriteria getSearchCriteriaForFindingIpsToBlock(String[] args) {
        Map<String,String> argsMap = Args.getArgsMap(args);
        return SearchCriteria.getInstance(argsMap);
    }

    private void checkIfFound(List<Long> iPsToBlock, List<Long> expectedIpsToBlock) {
        assertNotNull(iPsToBlock);
        assertTrue(iPsToBlock.size() == expectedIpsToBlock.size());
        assertArrayEquals(expectedIpsToBlock.toArray(), iPsToBlock.toArray());
    }

    // ---- daily duration tests ----

    @Test
    public void testFindIpsToBlock_oneFound_dailyDuration() {
        blockingCriteria =
            getSearchCriteriaForFindingIpsToBlock(
                withArgsForFindingOneIpForDay(DAILY, THRESHOLD_TO_FIND_ONE_IP_FOR_DAY));
        testFindingIpsToBlock(blockingCriteria, expectedSingleBlockedIpForDay);
    }

    private String[] withArgsForFindingOneIpForDay(Duration duration, int threshold) {
        return getArgs(duration, threshold);
    }

    @Test
    public void testFindIpsToBlock_multipleFound_dailyDuration() {
        blockingCriteria =
            getSearchCriteriaForFindingIpsToBlock(
                withArgsForFindingMultipleIpsForDay(DAILY, THRESHOLD_TO_FIND_MULTIPLE_IPS_FOR_DAY));
        testFindingIpsToBlock(blockingCriteria, expectedMultipleBlockedIpsForDay);
    }

    private String[] withArgsForFindingMultipleIpsForDay(Duration duration, int threshold) {
        return getArgs(duration, threshold);
    }

    // ---- hourly duration tests ----

    @Test
    public void testFindIpsToBlock_oneFound_hourlyDuration() {
        blockingCriteria =
            getSearchCriteriaForFindingIpsToBlock(
                withArgsForFindingOneIpForHour(HOURLY, THRESHOLD_TO_FIND_ONE_IP_FOR_HOUR));
        testFindingIpsToBlock(blockingCriteria, expectedSingleBlockedIpForHour);
    }

    private String[] withArgsForFindingOneIpForHour(Duration duration, int threshold) {
        return getArgs(duration, threshold);
    }

    @Test
    public void testFindIpsToBlock_multipleFound_hourlyDuration() {
        blockingCriteria =
            getSearchCriteriaForFindingIpsToBlock(
                withArgsForFindingMultipleIpsForHour(HOURLY, THRESHOLD_TO_FIND_MULTIPLE_IPS_FOR_HOUR));
        testFindingIpsToBlock(blockingCriteria, expectedMultipleBlockedIpsForHour);
    }

    private String[] withArgsForFindingMultipleIpsForHour(Duration duration, int threshold) {
        return getArgs(duration, threshold);
    }

    // ------- save tests ------

    @Test
    public void testSaveIpsToBlock_saveEmptyList() {
        blockingCriteriaForComment =
            getSearchCriteriaForFindingIpsToBlock(
                withArgsForFindingOneIpForHour(HOURLY, THRESHOLD_TO_FIND_ONE_IP_FOR_HOUR));
        List<Long> emptyListOfIps = testNotFindingAnyIpsToBlock();
        logEntryStore.saveIpsToBlock(blockingCriteriaForComment, emptyListOfIps);
        verifyBlockedIpsWhereSaved(Collections.EMPTY_LIST);
    }

    private void verifyBlockedIpsWhereSaved(List<Long> ipsExpectedToBeSaved) {
        List<Long> savedIps = readSavedIpsFromStore();
        assertTrue(ipsExpectedToBeSaved.size() == savedIps.size());
        assertArrayEquals(ipsExpectedToBeSaved.toArray(), savedIps.toArray());
    }

    private List<Long> readSavedIpsFromStore() {
        List<Long> blockedIps;
        try {
            blockedIps = blockedIpManager.stream()
                .mapToLong(BlockedIp::getIpAddress)
                .sorted()
                .boxed()
                .collect(Collectors.toList());
        } catch (SpeedmentException e) {
            throw new LogHandlerException("Failure fetching blocked IPs for test verification.", e);
        }
        assertNotNull(blockedIps);
        return blockedIps;
    }

    @Test
    public void testSaveIpsToBlock_saveListWithOneIp() {
        blockingCriteriaForComment =
            getSearchCriteriaForFindingIpsToBlock(
                withArgsForFindingOneIpForHour(HOURLY, THRESHOLD_TO_FIND_ONE_IP_FOR_HOUR));
        List<Long> listWithOneIp =
            testFindingIpsToBlock(blockingCriteriaForComment, expectedSingleBlockedIpForHour);
        logEntryStore.saveIpsToBlock(blockingCriteriaForComment, listWithOneIp);
        verifyBlockedIpsWhereSaved(expectedSingleBlockedIpForHour);
    }

    @Test
    public void testSaveIpsToBlock_saveListWithMultipleIps() {
        blockingCriteriaForComment =
            getSearchCriteriaForFindingIpsToBlock(
                withArgsForFindingMultipleIpsForHour(HOURLY, THRESHOLD_TO_FIND_MULTIPLE_IPS_FOR_HOUR));
        List<Long> listOfMultipleIps =
            testFindingIpsToBlock(blockingCriteriaForComment, expectedMultipleBlockedIpsForHour);
        logEntryStore.saveIpsToBlock(blockingCriteriaForComment, listOfMultipleIps);
        verifyBlockedIpsWhereSaved(expectedMultipleBlockedIpsForHour);
    }

    @Test(expected = LogHandlerException.class)
    public void testSaveIpsToBlock_saveFailure() {
        blockingCriteriaForComment =
            getSearchCriteriaForFindingIpsToBlock(withArgsForFindingNothing(HOURLY, THRESHOLD_200));
        List<Long> listWithInvalidIp = Collections.singletonList(-2147483648L);
        logEntryStore.saveIpsToBlock(blockingCriteriaForComment, listWithInvalidIp);
    }
}
