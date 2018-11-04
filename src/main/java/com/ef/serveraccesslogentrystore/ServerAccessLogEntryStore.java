/*
 * Created by Luke DeRienzo on 10/31/18 12:30 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 10/31/18 12:26 PM
 */

package com.ef.serveraccesslogentrystore;

import java.util.List;


interface ServerAccessLogEntryStore<E> {
    void loadFile(String path);
    List<E> findIpsToBlock(SearchCriteria blockingCriteria);
    void saveIpsToBlock(SearchCriteria blockingCriteriaForComment, List<E> ips);
}
