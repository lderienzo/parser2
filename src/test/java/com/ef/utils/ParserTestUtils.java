/*
 * Created by Luke DeRienzo on 8/15/18 12:29 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 8/15/18 11:50 AM
 */

package com.ef.utils;


import java.io.File;

public final class ParserTestUtils {

    public static String getAbsoluteFilePathFromClassResourceLoader(ClassLoader classLoader, String path) {
        return new File(classLoader.getResource(path).getFile()).getAbsolutePath();
    }

    public static ClassLoader getThisObjectsClassLoader(Object objectProvidingClassLoader) {
        return objectProvidingClassLoader.getClass().getClassLoader();
    }
}
