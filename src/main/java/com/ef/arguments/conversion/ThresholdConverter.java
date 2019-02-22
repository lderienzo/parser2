/*
 * Created by Luke DeRienzo on 12/19/18 11:21 AM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 12/18/18 11:20 PM
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
