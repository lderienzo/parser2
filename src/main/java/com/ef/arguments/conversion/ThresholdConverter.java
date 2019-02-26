/*
 * Created by Luke DeRienzo on 2/25/19 9:34 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 1/25/19 2:44 PM
 */

package com.ef.arguments.conversion;

import static com.ef.constants.Constants.THRESHOLD_CONVERTER_ERR_MSG_PREFIX;
import static com.ef.constants.Constants.THRESHOLD_CONVERTER_ERR_MSG_SUFFIX;

import com.ef.arguments.ArgsException;

public class ThresholdConverter implements StringTypeConverter<Integer> {
    private Integer convertedValue;

    @Override
    public Integer convert(String valueName, String value) {
        makeSureValueIsPresent(valueName, value);
        convertStringToInteger(value);
        return convertedValue;
    }

    private void convertStringToInteger(String value) {
        try {
            convertedValue = Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new ArgsException(THRESHOLD_CONVERTER_ERR_MSG_PREFIX + value + THRESHOLD_CONVERTER_ERR_MSG_SUFFIX, ex);
        }
    }
}
