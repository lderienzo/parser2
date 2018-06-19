package com.ef.arguments;

import static com.ef.enums.Duration.DAILY;
import static com.ef.enums.Duration.HOURLY;


import com.google.common.base.Strings;

public class DurationArgHandler implements ArgHandler {

    @Override
    public <T> T getValue(String strVal, Class<T> clazz) throws ArgsException {

        if (Strings.isNullOrEmpty(strVal) || (!strVal.equals(HOURLY.toString())
                && !strVal.equals(DAILY.toString()))) {
            System.out.println("Error: Invalid value entered for 'duration'!");
            throw new ArgsException("Invalid duration value.");
        }

        T[] durationEnums = clazz.getEnumConstants();
        if (durationEnums.length != 2) {
            throw new ArgsException("Error - internal error processing 'Duration' enums");
        }

        T defaultDuration = durationEnums[0];    // default value set to HOURLY
        if (durationEnums[1].toString().equals(strVal)) {   // strVal equals DAILY
            return durationEnums[1];
        }

        return defaultDuration;
    }
}
