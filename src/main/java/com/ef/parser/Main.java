package com.ef.parser;

import com.ef.parser.db.ParserApplication;
import com.ef.parser.db.ParserApplicationBuilder;
import com.ef.parser.db.parser.parser.access_log.AccessLogManager;

public class Main {
    public static void main(String... param) {
        // initialize db connection...
        ParserApplication app = new ParserApplicationBuilder()
                .withPassword("password").build();

        AccessLogManager accessLog = app.getOrThrow(AccessLogManager.class);
//        accessLog.persist()
    }
}
