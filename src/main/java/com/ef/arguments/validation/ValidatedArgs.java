/*
 * Created by Luke DeRienzo on 2/25/19 9:34 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 2/5/19 1:21 PM
 */

package com.ef.arguments.validation;


import com.ef.blockedipstore.SearchCriteria;

public final class ValidatedArgs {
    private final SearchCriteria ipBlockingCriteria;
    private final String accessLog;


    public ValidatedArgs(String accessLog, SearchCriteria ipBlockingCriteria) {
        this.accessLog = accessLog;
        this.ipBlockingCriteria = ipBlockingCriteria;
    }

    public SearchCriteria getIpBlockingSearchCriteria() {
        return ipBlockingCriteria;
    }

    public String getAccessLog() {
        return accessLog;
    }
}
