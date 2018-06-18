package com.ef.parser;

public class ArgsException extends Exception {

    public ArgsException(String message) {
        super(message);
    }

    public String errorMessage() {
        return getMessage();
    }
}

