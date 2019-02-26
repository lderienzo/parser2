/*
 * Created by Luke DeRienzo on 2/25/19 9:34 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 2/5/19 12:59 PM
 */

package com.ef.arguments.conversion;

import static com.ef.constants.Constants.STRING_TYPE_CONVERTER_VALUE_ABSENT_ERR_MSG;

import com.ef.arguments.ArgsException;
import com.google.common.base.Strings;

interface StringTypeConverter<T> {

    T convert(String valueName, String value);

    default void makeSureValueIsPresent(String valueName, String value) {
        if (Strings.isNullOrEmpty(value))
            throw new ArgsException("Error processing argument for ["+valueName+"]. " + STRING_TYPE_CONVERTER_VALUE_ABSENT_ERR_MSG);
    }
}
