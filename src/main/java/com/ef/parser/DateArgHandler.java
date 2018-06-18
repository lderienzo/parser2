package com.ef.parser;

import static com.ef.parser.ParserUtils.ARGUMENT_DATE_FORMATTER;

import java.time.LocalDateTime;


public class DateArgHandler implements ArgHandler<LocalDateTime> {

    @Override
    public LocalDateTime getValue(String dateStr) {
        return LocalDateTime.parse(dateStr, ARGUMENT_DATE_FORMATTER);
    }
}
