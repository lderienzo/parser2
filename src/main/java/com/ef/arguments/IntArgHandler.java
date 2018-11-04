/*
 * Created by Luke DeRienzo on 8/15/18 12:29 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 6/23/18 12:00 AM
 */

package com.ef.arguments;

import com.google.common.base.Strings;

public class IntArgHandler implements ArgHandler {

    @Override
    public <T> T getValue(String strVal, Class<T> clazz) {
        Integer threshold;
        try {
            if (Strings.isNullOrEmpty(strVal)) {
                throw new ArgsException("Failure in IntArgHandler::getValue. Value for threshold is null or empty.");
            }
            threshold = Integer.parseInt(strVal);
        } catch (NumberFormatException e) {
            throw new ArgsException("Failure in IntArgHandler::getValue. Value for threshold is not an integer.", e);
        }
        if (outsideBounds(threshold)) {
            throw new ArgsException("Failure in IntArgHandler::getValue. Value for threshold is outside allowable bounds of 100 to 500.");
        }
        return (T)threshold;
    }

    private boolean outsideBounds(int threshold) {
        return (threshold < 0 || threshold > 10000);
    }
}
