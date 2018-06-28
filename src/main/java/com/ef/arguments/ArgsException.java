package com.ef.arguments;

public class ArgsException extends RuntimeException {

    public ArgsException(String message, Exception e) {
        super(message, e);
    }

    public ArgsException(String message) {
        super(message);
    }
}

