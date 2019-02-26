/*
 * Created by Luke DeRienzo on 2/25/19 9:34 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 2/25/19 6:03 PM
 */

package com.ef.arguments;

class CommandLineArgsEmulatorException extends RuntimeException {
    private static final long serialVersionUID = 2196071120022239111L;

    public CommandLineArgsEmulatorException(String message, Exception e) {
        super(message, e);
    }
}
