/*
 * Created by Luke DeRienzo on 8/15/18 12:29 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 8/15/18 11:50 AM
 */

package com.ef.utils;


import java.io.File;
import java.net.URL;

public final class TestUtils {

    public static String getAbsoluteFilePathFromClassResourceLoader(ClassLoader classLoader, String path) {
        String absoluteFilePath = "";
        URL resourceUrl = classLoader.getResource(path);
        if (resourceUrl == null) {
            return absoluteFilePath;
        }
        File file = new File(resourceUrl.getFile());
        if (file == null) {
            return absoluteFilePath;
        }
        absoluteFilePath = file.getAbsolutePath();
        return absoluteFilePath;
    }


    public static ClassLoader getThisObjectsClassLoader(Object objectProvidingClassLoader) {
        return objectProvidingClassLoader.getClass().getClassLoader();
    }
}
