package com.ef;

import java.io.File;

import com.google.common.base.Strings;


public class FilePathArgHandler implements ArgHandler<String> {

    @Override
    public String getValue(String pathStr) throws ArgsException {
        if (!Strings.isNullOrEmpty(pathStr)) {
            File file = new File(pathStr);
            if (!file.exists() || !file.isFile()) {
                throw new ArgsException("File not found!");
            }
        }
        else {
            pathStr = "";
        }
        return pathStr;
    }
}
