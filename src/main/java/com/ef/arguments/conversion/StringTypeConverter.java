/*
 * Created by Luke DeRienzo on 12/19/18 11:21 AM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 12/18/18 11:10 PM
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
