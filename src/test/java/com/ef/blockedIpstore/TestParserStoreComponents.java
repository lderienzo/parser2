/*
 * Created by Luke DeRienzo on 1/24/19 10:14 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 1/24/19 10:14 PM
 */

package com.ef.blockedIpstore;


import com.ef.blockedipstore.SpeedmentStoreComponents;
import com.ef.db.ParserApplicationBuilder;
import com.speedment.runtime.core.ApplicationBuilder;

public class TestParserStoreComponents extends SpeedmentStoreComponents {

    private TestParserStoreComponents() {
        super.initialize();
    }

    private static class SingletonHolder {
        static final TestParserStoreComponents instance = new TestParserStoreComponents();
    }

    public static TestParserStoreComponents getSingletonInstance(String pwd) {
        SpeedmentStoreComponents.password = pwd;
        return TestParserStoreComponents.SingletonHolder.instance;
    }

    @Override
    protected void subClassInitializesDbConnection() {
        appDb = new ParserApplicationBuilder()
                .withPassword(password)
                .withLogging(ApplicationBuilder.LogType.PERSIST)
                .withLogging(ApplicationBuilder.LogType.STREAM)
                .withLogging(ApplicationBuilder.LogType.REMOVE)
                .build();
    }
}
