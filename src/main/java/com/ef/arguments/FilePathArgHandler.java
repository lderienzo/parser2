package com.ef.arguments;

import java.io.File;

import com.google.common.base.Strings;


public class FilePathArgHandler implements ArgHandler {

    @Override
    public <T> T getValue(String strVal, Class<T> clazz) {
        if (Strings.isNullOrEmpty(strVal)) {
            strVal = "";
            return (T)strVal;
        }
        if (fileExists(new File(strVal))) {
            return (T)strVal;
        }
        else {
            throw new ArgsException("Failure in FilePathArgHandler::getValue. File not found.");
        }
    }

    private boolean fileExists(File file) {
        return (file.exists() && file.isFile());
    }
}
