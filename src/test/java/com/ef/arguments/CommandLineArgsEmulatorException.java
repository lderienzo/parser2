/*
 * Created by Luke DeRienzo on 11/6/18 1:00 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 11/5/18 9:48 PM
 */

package com.ef.arguments;

class CommandLineArgsEmulatorException extends RuntimeException {
    private static final long serialVersionUID = 2196071120022239111L;

    public CommandLineArgsEmulatorException(String message, Exception e) {
        super(message, e);
    }
}
