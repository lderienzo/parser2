/*
 * Created by Luke DeRienzo on 2/25/19 4:27 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 2/25/19 4:27 PM
 */

package com.ef.blockedIpstore.config;

import java.util.List;

import com.ef.blockedipstore.BlockedIpStore;

interface BlockedIpStoreForTesting extends BlockedIpStore {
    List<Long> readBlockedIps();
    long countLogEntries();
    long countBlockedIps();
    void clearLogEntries();
    void clearBlockedIps();
    void shutDownStore();
}
