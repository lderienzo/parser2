/*
 * Created by Luke DeRienzo on 10/31/18 12:30 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 10/31/18 12:26 PM
 */

package com.ef.serveraccesslogentrystore;

import java.util.List;

// TODO: rename SeverAccessEntryLogStore to BlockedIpStore and LogEntryStore to LogStore?
public interface ServerAccessLogEntryStore<E> { // BlockedIpStore
    void loadFile(String path); // loadServerAccessLog(String filePath)
    List<E> findIpsToBlock(SearchCriteria blockingCriteria);
    void saveIpsToBlock(SearchCriteria blockingCriteriaForComment, List<E> ips);    // saveIpsToBlock(String reason, List ips) - have search criteria provide reason string
}
