/*
 * Created by Luke DeRienzo on 12/12/18 11:14 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 12/12/18 11:14 PM
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
