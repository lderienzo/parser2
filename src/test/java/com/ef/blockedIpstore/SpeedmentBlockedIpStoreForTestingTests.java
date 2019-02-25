/*
 * Created by Luke DeRienzo on 2/25/19 4:22 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 2/25/19 4:22 PM
 */

package com.ef.blockedIpstore;

import static com.ef.constants.Constants.STORE_PASSWORD;
import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.Test;

import com.ef.blockedIpstore.config.SpeedmentBlockedIpStoreForTesting;
import com.ef.blockedipstore.SpeedmentBlockedIpStore;
import com.ef.config.TestConfig;

public class SpeedmentBlockedIpStoreForTestingTests {
    private static final SpeedmentBlockedIpStoreForTesting TEST_SPEEDMENT_BLOCKED_IP_STORE = TestConfig.getTestStore();

    @AfterClass
    public static void clearDB() {
        TEST_SPEEDMENT_BLOCKED_IP_STORE.shutDownStore();
    }

    @Test
    public void testThatSpeedmentBlockedIpStoreIsSingleton() {
        SpeedmentBlockedIpStoreForTesting testSpeedmentBlockedIpStore2 =
                SpeedmentBlockedIpStoreForTesting.getSingletonInstance(STORE_PASSWORD,
                        SpeedmentBlockedIpStore.getSingletonInstance(STORE_PASSWORD));

        assertEquals(TEST_SPEEDMENT_BLOCKED_IP_STORE, testSpeedmentBlockedIpStore2);
    }
}
