package com.ef;

public class ArgsException extends Exception {

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

