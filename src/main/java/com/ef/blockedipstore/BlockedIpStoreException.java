/*
 * Created by Luke DeRienzo on 1/6/19 9:46 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 1/3/19 7:55 PM
 */

package com.ef.blockedipstore;

public final class BlockedIpStoreException extends RuntimeException {
    private static final long serialVersionUID = 3340912774406754032L;

    public BlockedIpStoreException(String message, Exception e) {
        super(message, e);
    }
}
