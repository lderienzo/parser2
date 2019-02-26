/*
 * Created by Luke DeRienzo on 2/25/19 9:34 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 2/25/19 4:31 PM
 */

package com.ef.config;

import static com.ef.constants.Constants.STORE_PASSWORD;

import com.ef.blockedIpstore.config.SpeedmentBlockedIpStoreForTesting;
import com.ef.blockedipstore.SpeedmentBlockedIpStore;

public final class TestConfig {

    private TestConfig () {}

    public static SpeedmentBlockedIpStoreForTesting getTestStore() {
        return SpeedmentBlockedIpStoreForTesting.getSingletonInstance(STORE_PASSWORD,
                        SpeedmentBlockedIpStore.getSingletonInstance(STORE_PASSWORD));
    }

}
