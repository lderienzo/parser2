/*
 * Created by Luke DeRienzo on 8/15/18 12:29 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 8/15/18 11:50 AM
 */

package com.ef.utils;


import java.io.File;

public final class TestUtils {

    public static String getAbsoluteFilePathFromClassResourceLoader(ClassLoader classLoader, String path) {
        String absolutePath = "";
        File file = new File(classLoader.getResource(path).getFile());
        if (file != null) {
            absolutePath = file.getAbsolutePath();
        }
        return absolutePath;
    }

    public static ClassLoader getThisObjectsClassLoader(Object objectProvidingClassLoader) {
        return objectProvidingClassLoader.getClass().getClassLoader();
    }
}
