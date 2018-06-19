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

        File file = new File(strVal);
        if (!file.exists() || !file.isFile()) {
            System.out.println("Error: File not found! Please re-enter.");
            throw new ArgsException("Invalid file path.");
        }

        return (T)strVal;
    }
}
