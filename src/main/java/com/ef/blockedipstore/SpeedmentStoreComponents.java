/*
 * Created by Luke DeRienzo on 1/25/19 12:13 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 1/25/19 12:13 PM
 */

package com.ef.blockedipstore;

import com.ef.db.ParserApplication;
import com.ef.db.parser.parser.access_log_entry.AccessLogEntryManager;
import com.ef.db.parser.parser.blocked_ip.BlockedIpManager;

public abstract class SpeedmentStoreComponents {
    public static String password;
    public ParserApplication appDb;
    public AccessLogEntryManager logEntryManager;
    public BlockedIpManager blockedIpManager;

    public void initialize() {
        subClassInitializesDbConnection();
        initializeEntityManagersUsingAppDb();
    }

    protected abstract void subClassInitializesDbConnection();

    private void initializeEntityManagersUsingAppDb() {
        setLogEntryManagerStoreComponent();
        setBlockedIpManagerStoreComponent();
    }

    private void setLogEntryManagerStoreComponent() {
        logEntryManager = getManagerFromAppDbComponent(AccessLogEntryManager.class);
    }

    private <T> T getManagerFromAppDbComponent(Class<T> aClass) {
        return appDb.getOrThrow(aClass);
    }

    private void setBlockedIpManagerStoreComponent() {
        blockedIpManager = getManagerFromAppDbComponent(BlockedIpManager.class);
    }
}
