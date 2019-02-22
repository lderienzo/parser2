/*
 * Created by Luke DeRienzo on 2/3/19 1:13 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 2/3/19 1:13 PM
 */

package com.ef.blockedIpstore;

import static com.ef.constants.Constants.STORE_PASSWORD;
import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ef.blockedipstore.SpeedmentBlockedIpStore;

public class ParserBlockedIpStoreAbstractParentForTesting {
    protected static TestSpeedmentBlockedIpStore testSpeedmentBlockedIpStore;

    @BeforeClass
    public static void initApp() {
        testSpeedmentBlockedIpStore =
            TestSpeedmentBlockedIpStore.getSingletonInstance(STORE_PASSWORD,
                SpeedmentBlockedIpStore.getSingletonInstance(STORE_PASSWORD));

    }

    @AfterClass
    public static void clearDB() {
        testSpeedmentBlockedIpStore.clearLogEntries();
        testSpeedmentBlockedIpStore.clearBlockedIps();
        testSpeedmentBlockedIpStore.shutdownStore();
    }

    @Test
    public void testThatSpeedmentBlockedIpStoreIsSingleton() {
        TestSpeedmentBlockedIpStore testSpeedmentBlockedIpStore2 =
            TestSpeedmentBlockedIpStore.getSingletonInstance(STORE_PASSWORD,
                SpeedmentBlockedIpStore.getSingletonInstance(STORE_PASSWORD));

        assertEquals(testSpeedmentBlockedIpStore, testSpeedmentBlockedIpStore2);
    }
}
