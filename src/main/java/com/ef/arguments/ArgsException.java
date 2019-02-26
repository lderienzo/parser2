/*
 * Created by Luke DeRienzo on 2/25/19 9:34 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 2/25/19 5:59 PM
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

