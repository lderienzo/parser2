/*
 * Created by Luke DeRienzo on 2/25/19 9:34 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 2/24/19 12:09 PM
 */

package com.ef.arguments.conversion;

import static com.ef.constants.Constants.START_DATE_CONVERTER_ERR_MSG_PREFIX;
import static com.ef.constants.Constants.START_DATE_CONVERTER_ERR_MSG_SUFFIX;
import static com.ef.constants.Constants.ARGUMENT_DATE_FORMATTER;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import com.ef.arguments.ArgsException;

public class StartDateConverter implements StringTypeConverter<LocalDateTime> {
    private LocalDateTime convertedValue;

    @Override
    public LocalDateTime convert(String valueName, String value) {
        makeSureValueIsPresent(valueName, value);
        convertStringToLocalDateTime(value);
        return convertedValue;
    }

    private void convertStringToLocalDateTime(String value) {
        try {
            convertedValue = LocalDateTime.parse(value, ARGUMENT_DATE_FORMATTER);
        } catch (DateTimeParseException ex) {
            throw new ArgsException(START_DATE_CONVERTER_ERR_MSG_PREFIX + value + START_DATE_CONVERTER_ERR_MSG_SUFFIX, ex);
        }
    }
}
