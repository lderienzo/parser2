package com.ef.parser;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class LogReaderTest {
    LogReader logReader;

    @Before
    public void setUp() {
        logReader = new AccessLogReader();
    }

    @Test
    public void testLogReader() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("test_access.log").getFile());
        logReader.read(file.getAbsolutePath());
    }
}
