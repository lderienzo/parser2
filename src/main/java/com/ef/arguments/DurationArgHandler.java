/*
 * Created by Luke DeRienzo on 8/15/18 12:29 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 6/23/18 11:02 AM
 */

package com.ef.arguments;

import static com.ef.enums.Duration.DAILY;
import static com.ef.enums.Duration.HOURLY;


import com.google.common.base.Strings;

public class DurationArgHandler implements ArgHandler {

    @Override
    public <T> T getValue(String strVal, Class<T> clazz) {
        T dailyDuration;
        T hourlyDuration;
        T[] durationEnums = clazz.getEnumConstants();
        if (durationEnums.length == 2 &&
                durationEnums[0].equals(HOURLY) && durationEnums[1].equals(DAILY)) {
            hourlyDuration = durationEnums[0];
            dailyDuration = durationEnums[1];
        }
        else {
            throw new ArgsException("Failure in DurationArgHandler::getValue. Unable to decipher enum values.");
        }
        if (isNonEmpty(strVal) && strVal.equals(dailyDuration.toString())) {
            return dailyDuration;
        }
        else if (isNonEmpty(strVal) && strVal.equals(hourlyDuration.toString())) {
            return hourlyDuration;
        }
        else {
            throw new ArgsException("Failure in DurationArgHandler::getValue. Invalid duration value ["+strVal+"].");
        }
    }

    private boolean isNonEmpty(String strVal) {
        return !Strings.isNullOrEmpty(strVal);
    }
}
