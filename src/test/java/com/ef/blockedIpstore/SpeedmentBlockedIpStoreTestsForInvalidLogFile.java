/*
 * Created by Luke DeRienzo on 1/2/19 5:30 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 1/2/19 5:30 PM
 */

package com.ef.blockedIpstore;

import static com.ef.constants.Constants.LOG_ENTRY_STORE_ERR_LOADING_LOG_FILE;
import static com.ef.utils.ParserTestUtils.BOGUS_TEST_LOG_FILE_PATH;
import static com.ef.utils.ParserTestUtils.INVALID_LINE_FORMAT_LOG_FILE;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.ef.blockedipstore.BlockedIpStoreException;
import com.ef.utils.ParserTestUtils;


public class SpeedmentBlockedIpStoreTestsForInvalidLogFile extends ParserBlockedIpStoreAbstractParentForTesting {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();


    @Test
    public void testLoadFile_nonExistentFileShouldThrowLogEntryStoreException() {
        thrown.expect(BlockedIpStoreException.class);
        thrown.expectMessage(LOG_ENTRY_STORE_ERR_LOADING_LOG_FILE);

        testSpeedmentBlockedIpStore.loadFile(BOGUS_TEST_LOG_FILE_PATH);
    }

    @Test
    public void testLoadFile_invalidFileFormatShouldThrowLogEntryStoreException() {
        thrown.expect(BlockedIpStoreException.class);
        thrown.expectMessage(LOG_ENTRY_STORE_ERR_LOADING_LOG_FILE);

        testSpeedmentBlockedIpStore.loadFile(
            ParserTestUtils.getAbsoluteFilePathFromClassResourceLoader(
                    ParserTestUtils.getThisObjectsClassLoader(this),
                    INVALID_LINE_FORMAT_LOG_FILE));
    }

    @Test
    public void testLoadFile_emptyFilePathShouldThrowLogEntryStoreException() {
        thrown.expect(BlockedIpStoreException.class);
        thrown.expectMessage(LOG_ENTRY_STORE_ERR_LOADING_LOG_FILE);

        testSpeedmentBlockedIpStore.loadFile("");
    }

    @Test
    public void testLoadFile_nullFilePathShouldThrowLogEntryStoreException() {
        thrown.expect(BlockedIpStoreException.class);
        thrown.expectMessage(LOG_ENTRY_STORE_ERR_LOADING_LOG_FILE);

        testSpeedmentBlockedIpStore.loadFile(null);
    }
}
