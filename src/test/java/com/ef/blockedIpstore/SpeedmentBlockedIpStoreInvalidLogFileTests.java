/*
 * Created by Luke DeRienzo on 1/2/19 5:30 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 1/2/19 5:30 PM
 */

package com.ef.blockedIpstore;

import static com.ef.constants.Constants.LOG_ENTRY_STORE_ERR_LOADING_LOG_FILE;
import static com.ef.constants.TestConstants.BOGUS_TEST_LOG_FILE_PATH;
import static com.ef.constants.TestConstants.INVALID_LINE_FORMAT_LOG_FILE;


import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.ef.blockedIpstore.config.SpeedmentBlockedIpStoreForTesting;
import com.ef.blockedipstore.BlockedIpStoreException;
import com.ef.config.TestConfig;
import com.ef.utils.ParserTestUtils;


public class SpeedmentBlockedIpStoreInvalidLogFileTests {
    private static final SpeedmentBlockedIpStoreForTesting TEST_SPEEDMENT_BLOCKED_IP_STORE = TestConfig.getTestStore();

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @AfterClass
    public static void clearDB() {
        TEST_SPEEDMENT_BLOCKED_IP_STORE.shutDownStore();
    }

    @Test
    public void testLoadFile_nonExistentFileShouldThrowLogEntryStoreException() {
        thrown.expect(BlockedIpStoreException.class);
        thrown.expectMessage(LOG_ENTRY_STORE_ERR_LOADING_LOG_FILE);

        TEST_SPEEDMENT_BLOCKED_IP_STORE.loadFile(BOGUS_TEST_LOG_FILE_PATH);
    }

    @Test
    public void testLoadFile_invalidFileFormatShouldThrowLogEntryStoreException() {
        thrown.expect(BlockedIpStoreException.class);
        thrown.expectMessage(LOG_ENTRY_STORE_ERR_LOADING_LOG_FILE);

        TEST_SPEEDMENT_BLOCKED_IP_STORE.loadFile(
            ParserTestUtils.getAbsoluteFilePathFromClassResourceLoader(
                    ParserTestUtils.getThisObjectsClassLoader(this),
                    INVALID_LINE_FORMAT_LOG_FILE));
    }

    @Test
    public void testLoadFile_emptyFilePathShouldThrowLogEntryStoreException() {
        thrown.expect(BlockedIpStoreException.class);
        thrown.expectMessage(LOG_ENTRY_STORE_ERR_LOADING_LOG_FILE);

        TEST_SPEEDMENT_BLOCKED_IP_STORE.loadFile("");
    }

    @Test
    public void testLoadFile_nullFilePathShouldThrowLogEntryStoreException() {
        thrown.expect(BlockedIpStoreException.class);
        thrown.expectMessage(LOG_ENTRY_STORE_ERR_LOADING_LOG_FILE);

        TEST_SPEEDMENT_BLOCKED_IP_STORE.loadFile(null);
    }
}
