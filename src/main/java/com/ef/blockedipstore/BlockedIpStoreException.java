/*
 * Created by Luke DeRienzo on 2/25/19 9:34 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 2/25/19 6:03 PM
 */

package com.ef.blockedipstore;

public final class BlockedIpStoreException extends RuntimeException {
    private static final long serialVersionUID = 3340912774406754032L;

    public BlockedIpStoreException(String message, Exception e) {
        super(message, e);
    }
}
