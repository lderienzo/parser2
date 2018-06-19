package com.ef.arguments;

public class ArgsException extends Exception {

    public ArgsException(String message, Exception e) {
        super(message, e);
    }

    public ArgsException(Exception e) {
        super(e);
    }

    public ArgsException(String message) {
        super(message);
    }

    public String errorMessage() {
        return getMessage();
    }
}

