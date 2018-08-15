/*
 * Created by Luke DeRienzo on 8/15/18 12:29 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 6/22/18 11:14 PM
 */

package com.ef.loghandler;

public class LogHandlerException extends RuntimeException {
    public LogHandlerException(String message, Exception e) {
        super(message, e);
    }
}
