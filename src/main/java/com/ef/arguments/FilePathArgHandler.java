package com.ef.arguments;

import java.io.File;

import com.google.common.base.Strings;


public class FilePathArgHandler implements ArgHandler {

    @Override
    public <T> T getValue(String strVal, Class<T> clazz) throws ArgsException {

        if (Strings.isNullOrEmpty(strVal)) {
            strVal = "";
            return (T)strVal;
        }

        if (fileDoesNotExist(new File(strVal))) {
            System.out.println("Error: File not found! Please re-enter.");
            throw new ArgsException("File not found.");
        }

        return (T)strVal;
    }

    private boolean fileDoesNotExist(File file) {
        return (!file.exists() || !file.isFile());
    }
}
