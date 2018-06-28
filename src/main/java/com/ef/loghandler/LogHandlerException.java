package com.ef.loghandler;

public class LogHandlerException extends RuntimeException {
    public LogHandlerException(String message, Exception e) {
        super(message, e);
    }
}
