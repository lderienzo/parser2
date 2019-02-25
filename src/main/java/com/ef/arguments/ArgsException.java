/*
 * Created by Luke DeRienzo on 8/15/18 12:29 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 6/22/18 11:59 PM
 */

package com.ef.arguments;

public class ArgsException extends RuntimeException {

    private static final long serialVersionUID = 1643157061016802927L;

    public ArgsException(String message, Exception e) {
        super(message, e);
    }

    public ArgsException(String message) {
        super(message);
    }
}

