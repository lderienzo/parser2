/*
 * Created by Luke DeRienzo on 10/30/18 2:58 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 10/30/18 2:58 PM
 */

package com.ef.blockedIpstore;

import static com.ef.arguments.enums.Duration.DAILY;
import static com.ef.arguments.enums.Duration.HOURLY;
import static com.ef.utils.ParserTestUtils.NUM_OF_TEST_ACCESS_LOG_FILE_ENTRIES;
import static com.ef.utils.ParserTestUtils.TEST_ACCESS_LOG_FILE;
import static com.ef.utils.ParserTestUtils.TEST_START_DATE;
import static com.ef.utils.ParserTestUtils.THRESHOLD_200;
import static com.ef.utils.ParserTestUtils.THRESHOLD_TO_FIND_MULTIPLE_IPS_FOR_DAY;
import static com.ef.utils.ParserTestUtils.THRESHOLD_TO_FIND_MULTIPLE_IPS_FOR_HOUR;
import static com.ef.utils.ParserTestUtils.THRESHOLD_TO_FIND_ONE_IP_FOR_DAY;
import static com.ef.utils.ParserTestUtils.THRESHOLD_TO_FIND_ONE_IP_FOR_HOUR;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ef.blockedipstore.BlockedIpStoreException;
import com.ef.blockedipstore.SearchCriteria;
import com.ef.arguments.enums.Duration;
import com.ef.utils.ParserTestUtils;
import com.ef.utils.ParserUtils;


public class SpeedmentBlockedIpStoreTestsWithLoadedLogFile extends ParserBlockedIpStoreAbstractParentForTesting {
    private SearchCriteria blockingCriteria;
    private SearchCriteria blockingCriteriaForComment;
    private final LocalDateTime startDate = ParserUtils.stringToLocalDateTime(TEST_START_DATE);
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


    @Before
    public void loadLogFileIntoDb() {
        String absolutePath =
                ParserTestUtils.getAbsoluteFilePathFromClassResourceLoader(
                    ParserTestUtils.getThisObjectsClassLoader(this), TEST_ACCESS_LOG_FILE);

        testSpeedmentBlockedIpStore.loadFile(absolutePath);

        checkLogFileWasLoaded();
    }

    private void checkLogFileWasLoaded() {
        assertEquals(testSpeedmentBlockedIpStore.countLogEntries(), NUM_OF_TEST_ACCESS_LOG_FILE_ENTRIES);
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
        return testSpeedmentBlockedIpStore != null;
    }

    private void clearAccessLogTable() {
        testSpeedmentBlockedIpStore.clearLogEntries();
    }

    private void clearBlockedIpTable() {
        testSpeedmentBlockedIpStore.clearBlockedIps();
    }

    private void checkDbWasCleared() {
        long numberOfLogEntries = testSpeedmentBlockedIpStore.countLogEntries();
        long numberOfBlockedIpEntries = testSpeedmentBlockedIpStore.countBlockedIps();
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
        List<Long> iPsToBlock = testSpeedmentBlockedIpStore.findIpsToBlock(searchCriteria);
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

        testFindingIpsToBlock(blockingCriteria, expectedMultipleBlockedIpsForHour);
    }

    // ------- save tests ------

    @Test
    public void testSaveIpsToBlock_saveEmptyList() {
        blockingCriteriaForComment =
            getSearchCriteriaForFindingIpsToBlock(startDate, HOURLY, THRESHOLD_TO_FIND_ONE_IP_FOR_HOUR);

        List<Long> emptyListOfIps = testNotFindingAnyIpsToBlock();
        testSpeedmentBlockedIpStore.saveIpsToBlock(blockingCriteriaForComment, emptyListOfIps);

        verifyBlockedIpsWhereSaved(Collections.EMPTY_LIST);
    }

    private void verifyBlockedIpsWhereSaved(List<Long> ipsExpectedToBeSaved) {
        List<Long> savedIps = testSpeedmentBlockedIpStore.readBlockedIps();
        assertEquals(ipsExpectedToBeSaved.size(), savedIps.size());
        assertArrayEquals(ipsExpectedToBeSaved.toArray(), savedIps.toArray());
    }

    @Test
    public void testSaveIpsToBlock_saveListWithOneIp() {
        blockingCriteriaForComment =
            getSearchCriteriaForFindingIpsToBlock(startDate, HOURLY, THRESHOLD_TO_FIND_ONE_IP_FOR_HOUR);

        List<Long> listWithOneIp = testFindingIpsToBlock(blockingCriteriaForComment, expectedSingleBlockedIpForHour);
        testSpeedmentBlockedIpStore.saveIpsToBlock(blockingCriteriaForComment, listWithOneIp);

        verifyBlockedIpsWhereSaved(expectedSingleBlockedIpForHour);
    }

    @Test
    public void testSaveIpsToBlock_saveListWithMultipleIps() {
        blockingCriteriaForComment =
            getSearchCriteriaForFindingIpsToBlock(startDate, HOURLY, THRESHOLD_TO_FIND_MULTIPLE_IPS_FOR_HOUR);

        List<Long> listOfMultipleIps =
            testFindingIpsToBlock(blockingCriteriaForComment, expectedMultipleBlockedIpsForHour);
        testSpeedmentBlockedIpStore.saveIpsToBlock(blockingCriteriaForComment, listOfMultipleIps);

        verifyBlockedIpsWhereSaved(expectedMultipleBlockedIpsForHour);
    }

    @Test(expected = BlockedIpStoreException.class)
    public void testSaveIpsToBlock_saveFailure() {
        blockingCriteriaForComment = getSearchCriteriaForFindingIpsToBlock(startDate, HOURLY, THRESHOLD_200);
        List<Long> listWithInvalidIp = Collections.singletonList(-2147483648L);

        testSpeedmentBlockedIpStore.saveIpsToBlock(blockingCriteriaForComment, listWithInvalidIp);
    }
}
