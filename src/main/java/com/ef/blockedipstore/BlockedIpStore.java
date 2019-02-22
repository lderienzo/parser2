/*
 * Created by Luke DeRienzo on 1/6/19 9:46 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 1/2/19 9:52 PM
 */

package com.ef.blockedipstore;


public interface BlockedIpStore extends ParserStore<Long> {
    void shutdownStore();
}
