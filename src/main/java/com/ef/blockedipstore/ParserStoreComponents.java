/*
 * Created by Luke DeRienzo on 1/31/19 8:10 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 1/31/19 8:10 PM
 */

package com.ef.blockedipstore;

import com.ef.db.ParserApplicationBuilder;

public final class ParserStoreComponents extends SpeedmentStoreComponents {

    private ParserStoreComponents() {
        super.initialize();
    }

    private static class SingletonHolder {
        static final ParserStoreComponents instance = new ParserStoreComponents();
    }

    public static ParserStoreComponents getSingletonInstance(String pwd) {
        SpeedmentStoreComponents.password = pwd;
        return ParserStoreComponents.SingletonHolder.instance;
    }

    @Override
    public void subClassInitializesDbConnection() {
        appDb = new ParserApplicationBuilder()
                        .withPassword(SpeedmentStoreComponents.password)
                        .build();
    }
}
