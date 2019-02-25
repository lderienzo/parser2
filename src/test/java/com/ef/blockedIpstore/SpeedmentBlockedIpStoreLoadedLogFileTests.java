/*
 * Created by Luke DeRienzo on 10/30/18 2:58 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 10/30/18 2:58 PM
 */

package com.ef.blockedIpstore;

import static com.ef.arguments.enums.Duration.DAILY;
import static com.ef.arguments.enums.Duration.HOURLY;
import static com.ef.constants.TestConstants.EXPECTED_MULTIPLE_BLOCKED_IPS_FOR_HOUR;
import static com.ef.constants.TestConstants.NUM_OF_TEST_ACCESS_LOG_FILE_ENTRIES;
import static com.ef.constants.TestConstants.TEST_ACCESS_LOG_FILE;
import static com.ef.constants.TestConstants.TEST_START_DATE;
import static com.ef.constants.TestConstants.THRESHOLD_200;
import static com.ef.constants.TestConstants.THRESHOLD_TO_FIND_MULTIPLE_IPS_FOR_DAY;
import static com.ef.constants.TestConstants.THRESHOLD_TO_FIND_MULTIPLE_IPS_FOR_HOUR;
import static com.ef.constants.TestConstants.THRESHOLD_TO_FIND_ONE_IP_FOR_DAY;
import static com.ef.constants.TestConstants.THRESHOLD_TO_FIND_ONE_IP_FOR_HOUR;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.ef.blockedIpstore.config.SpeedmentBlockedIpStoreForTesting;
import com.ef.blockedipstore.BlockedIpStoreException;
import com.ef.blockedipstore.SearchCriteria;
import com.ef.arguments.enums.Duration;
import com.ef.config.TestConfig;
import com.ef.constants.Constants;
import com.ef.utils.ParserTestUtils;


public class SpeedmentBlockedIpStoreLoadedLogFileTests {
    private SearchCriteria blockingCriteria;
    private SearchCriteria blockingCriteriaForComment;
    private static final SpeedmentBlockedIpStoreForTesting TEST_SPEEDMENT_BLOCKED_IP_STORE = TestConfig.getTestStore();
    private final LocalDateTime startDate = stringToLocalDateTime(TEST_START_DATE);
    private final List<Long> expectedSingleBlockedIpForDay = Collections.singletonList(3232278978L);
    private final List<Long> expectedSingleBlockedIpForHour = Collections.singletonList(3232278978L);
    private final List<Long> expectedMultipleBlockedIpsForDay =
        Stream.of(3232243984L, 3232245325L, 3232246155L, 3232248781L, 3232261768L,
                  3232268735L, 3232269563L, 3232272305L, 3232277240L,
                  3232278978L, 3232281022L, 3232286673L, 3232287599L,
                  3232288397L, 3232291594L, 3232295506L).collect(
        Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));


    private LocalDateTime stringToLocalDateTime(String date) {
        return LocalDateTime.parse(date, Constants.ARGUMENT_DATE_FORMATTER);
    }

    @AfterClass
    public static void clearDB() {
        TEST_SPEEDMENT_BLOCKED_IP_STORE.shutDownStore();
    }

    @Before
    public void loadLogFileIntoDb() {
        String absolutePath =
                ParserTestUtils.getAbsoluteFilePathFromClassResourceLoader(
                    ParserTestUtils.getThisObjectsClassLoader(this), TEST_ACCESS_LOG_FILE);

        TEST_SPEEDMENT_BLOCKED_IP_STORE.loadFile(absolutePath);

        checkLogFileWasLoaded();
    }

    private void checkLogFileWasLoaded() {
        assertEquals(TEST_SPEEDMENT_BLOCKED_IP_STORE.countLogEntries(), NUM_OF_TEST_ACCESS_LOG_FILE_ENTRIES);
    }

    @After
    public void clearDb() {
        if (logEntryStoreIsNonNull()) {
            clearAccessLogTable();
            clearBlockedIpTable();
            checkDbWasCleared();
        }
    }

    private boolean logEntryStoreIsNonNull() {
        return TEST_SPEEDMENT_BLOCKED_IP_STORE != null;
    }

    private void clearAccessLogTable() {
        TEST_SPEEDMENT_BLOCKED_IP_STORE.clearLogEntries();
    }

    private void clearBlockedIpTable() {
        TEST_SPEEDMENT_BLOCKED_IP_STORE.clearBlockedIps();
    }

    private void checkDbWasCleared() {
        long numberOfLogEntries = TEST_SPEEDMENT_BLOCKED_IP_STORE.countLogEntries();
        long numberOfBlockedIpEntries = TEST_SPEEDMENT_BLOCKED_IP_STORE.countBlockedIps();
        assertEquals(numberOfLogEntries, 0L);
        assertEquals(numberOfBlockedIpEntries, 0L);
    }

    // --- END DB FIXTURE CODE ---

    @Test
    public void testFindIpsToBlock_noneFound() {
        testNotFindingAnyIpsToBlock();
    }

    private List<Long> testNotFindingAnyIpsToBlock() {
        blockingCriteria = getSearchCriteriaForFindingIpsToBlock(startDate, HOURLY, THRESHOLD_200);
        return testFindingIpsToBlock(blockingCriteria, Collections.EMPTY_LIST);
    }

    private SearchCriteria getSearchCriteriaForFindingIpsToBlock(LocalDateTime startDate, Duration duration, int threshold) {
        return new SearchCriteria(startDate, duration, threshold);
    }

    private List<Long> testFindingIpsToBlock(SearchCriteria searchCriteria, List<Long> expectedBlockedIps) {
        List<Long> iPsToBlock = TEST_SPEEDMENT_BLOCKED_IP_STORE.findIpsToBlock(searchCriteria);
        checkIfFound(expectedBlockedIps, iPsToBlock);
        return iPsToBlock;
    }

    private void checkIfFound(List<Long> expectedIpsToBlock, List<Long> iPsToBlock) {
        assertNotNull(iPsToBlock);
        assertEquals(expectedIpsToBlock.size(), iPsToBlock.size());
        assertArrayEquals(expectedIpsToBlock.toArray(), iPsToBlock.toArray());
    }

    // ---- daily duration tests ----

    @Test
    public void testFindIpsToBlock_oneFound_dailyDuration() {
        blockingCriteria = getSearchCriteriaForFindingIpsToBlock(startDate, DAILY, THRESHOLD_TO_FIND_ONE_IP_FOR_DAY);

        testFindingIpsToBlock(blockingCriteria, expectedSingleBlockedIpForDay);
    }

    @Test
    public void testFindIpsToBlock_multipleFound_dailyDuration() {
        blockingCriteria =
            getSearchCriteriaForFindingIpsToBlock(startDate, DAILY, THRESHOLD_TO_FIND_MULTIPLE_IPS_FOR_DAY);

        testFindingIpsToBlock(blockingCriteria, expectedMultipleBlockedIpsForDay);
    }

    // ---- hourly duration tests ----

    @Test
    public void testFindIpsToBlock_oneFound_hourlyDuration() {
        blockingCriteria = getSearchCriteriaForFindingIpsToBlock(startDate, HOURLY, THRESHOLD_TO_FIND_ONE_IP_FOR_HOUR);

        testFindingIpsToBlock(blockingCriteria, expectedSingleBlockedIpForHour);
    }

    @Test
    public void testFindIpsToBlock_multipleFound_hourlyDuration() {
        blockingCriteria =
            getSearchCriteriaForFindingIpsToBlock(startDate, HOURLY, THRESHOLD_TO_FIND_MULTIPLE_IPS_FOR_HOUR);

        testFindingIpsToBlock(blockingCriteria, EXPECTED_MULTIPLE_BLOCKED_IPS_FOR_HOUR);
    }

    // ------- save tests ------

    @Test
    public void testSaveIpsToBlock_saveEmptyList() {
        blockingCriteriaForComment =
            getSearchCriteriaForFindingIpsToBlock(startDate, HOURLY, THRESHOLD_TO_FIND_ONE_IP_FOR_HOUR);

        List<Long> emptyListOfIps = testNotFindingAnyIpsToBlock();
        TEST_SPEEDMENT_BLOCKED_IP_STORE.saveIpsToBlock(blockingCriteriaForComment, emptyListOfIps);

        verifyBlockedIpsWhereSaved(Collections.EMPTY_LIST);
    }

    private void verifyBlockedIpsWhereSaved(List<Long> ipsExpectedToBeSaved) {
        List<Long> savedIps = TEST_SPEEDMENT_BLOCKED_IP_STORE.readBlockedIps();
        assertEquals(ipsExpectedToBeSaved.size(), savedIps.size());
        assertArrayEquals(ipsExpectedToBeSaved.toArray(), savedIps.toArray());
    }

    @Test
    public void testSaveIpsToBlock_saveListWithOneIp() {
        blockingCriteriaForComment =
            getSearchCriteriaForFindingIpsToBlock(startDate, HOURLY, THRESHOLD_TO_FIND_ONE_IP_FOR_HOUR);

        List<Long> listWithOneIp = testFindingIpsToBlock(blockingCriteriaForComment, expectedSingleBlockedIpForHour);
        TEST_SPEEDMENT_BLOCKED_IP_STORE.saveIpsToBlock(blockingCriteriaForComment, listWithOneIp);

        verifyBlockedIpsWhereSaved(expectedSingleBlockedIpForHour);
    }

    @Test
    public void testSaveIpsToBlock_saveListWithMultipleIps() {
        blockingCriteriaForComment =
            getSearchCriteriaForFindingIpsToBlock(startDate, HOURLY, THRESHOLD_TO_FIND_MULTIPLE_IPS_FOR_HOUR);

        List<Long> listOfMultipleIps =
            testFindingIpsToBlock(blockingCriteriaForComment, EXPECTED_MULTIPLE_BLOCKED_IPS_FOR_HOUR);
        TEST_SPEEDMENT_BLOCKED_IP_STORE.saveIpsToBlock(blockingCriteriaForComment, listOfMultipleIps);

        verifyBlockedIpsWhereSaved(EXPECTED_MULTIPLE_BLOCKED_IPS_FOR_HOUR);
    }

    @Test(expected = BlockedIpStoreException.class)
    public void testSaveIpsToBlock_saveFailure() {
        blockingCriteriaForComment = getSearchCriteriaForFindingIpsToBlock(startDate, HOURLY, THRESHOLD_200);
        List<Long> listWithInvalidIp = Collections.singletonList(-2147483648L);

        TEST_SPEEDMENT_BLOCKED_IP_STORE.saveIpsToBlock(blockingCriteriaForComment, listWithInvalidIp);
    }
}
