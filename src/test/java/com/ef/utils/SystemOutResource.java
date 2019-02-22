/*
 * Created by Luke DeRienzo on 1/2/19 10:48 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 1/2/19 10:47 PM
 */

package com.ef.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.rules.ExternalResource;

public class SystemOutResource extends ExternalResource {

    private PrintStream sysOut;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Override
    protected void before() {
        sysOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @Override
    protected void after() {
        System.setOut(sysOut);
    }

    @Override
    public String toString() {
        return outContent.toString();
    }
}
