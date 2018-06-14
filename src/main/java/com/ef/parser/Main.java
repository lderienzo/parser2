package com.ef.parser;

import com.ef.parser.db.ParserApplication;
import com.ef.parser.db.ParserApplicationBuilder;

public class Main {
    public static void main(String... param) {
        // initialize db connection...
        ParserApplication app = new ParserApplicationBuilder()
                .withPassword("password").build();
    }
}
