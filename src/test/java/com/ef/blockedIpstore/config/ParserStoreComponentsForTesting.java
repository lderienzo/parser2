/*
 * Created by Luke DeRienzo on 2/25/19 4:27 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 2/25/19 4:27 PM
 */

package com.ef.blockedIpstore.config;


import com.ef.blockedipstore.SpeedmentStoreComponents;
import com.ef.db.ParserApplicationBuilder;
import com.speedment.runtime.core.ApplicationBuilder;

public class ParserStoreComponentsForTesting extends SpeedmentStoreComponents {

    private ParserStoreComponentsForTesting() {
        super.initialize();
    }

    private static class SingletonHolder {
        static final ParserStoreComponentsForTesting instance = new ParserStoreComponentsForTesting();
    }

    public static ParserStoreComponentsForTesting getSingletonInstance(String pwd) {
        SpeedmentStoreComponents.password = pwd;
        return ParserStoreComponentsForTesting.SingletonHolder.instance;
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
