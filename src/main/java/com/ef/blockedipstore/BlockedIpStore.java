/*
 * Created by Luke DeRienzo on 2/25/19 9:34 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 2/2/19 1:04 PM
 */

package com.ef.blockedipstore;


public interface BlockedIpStore extends ParserStore<Long> {
    void shutdownStore();
}
