/*
 * Created by Luke DeRienzo on 2/2/19 1:35 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 2/2/19 1:35 PM
 */

package com.ef.blockedIpstore;

import java.util.List;

import com.ef.blockedipstore.BlockedIpStore;

interface TestBlockedIpStore extends BlockedIpStore {
    List<Long> readBlockedIps();
    long countLogEntries();
    long countBlockedIps();
    void clearLogEntries();
    void clearBlockedIps();
}
