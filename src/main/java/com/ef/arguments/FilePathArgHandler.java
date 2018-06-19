package com.ef.arguments;

import java.io.File;

import com.google.common.base.Strings;


public class FilePathArgHandler implements ArgHandler {

    @Override
    public <T> T getValue(String strArgVal, Class<T> clazz) throws ArgsException {
        if (!Strings.isNullOrEmpty(strArgVal)) {
            File file = new File(strArgVal);
            if (!file.exists() || !file.isFile()) {
                System.out.println("Error: File not found! Please re-enter.\n");
                throw new ArgsException("Invalid file path.");
            }
        }
        else {
            strArgVal = "";
        }
        return (T)strArgVal;
    }
}
