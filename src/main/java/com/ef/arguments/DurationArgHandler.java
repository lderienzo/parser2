package com.ef.arguments;

import static com.ef.enums.Duration.DAILY;
import static com.ef.enums.Duration.HOURLY;


import com.google.common.base.Strings;

public class DurationArgHandler implements ArgHandler {

    @Override
    public <T> T getValue(String strVal, Class<T> clazz) throws ArgsException {

        if (Strings.isNullOrEmpty(strVal) || notEqualToAnyEnum(strVal)) {
            System.out.println("Error: Invalid value entered for 'duration'!");
            throw new ArgsException("Invalid duration value.");
        }

        T[] durationEnums = clazz.getEnumConstants();
        if (durationEnums.length != 2) {
            throw new ArgsException("Error - internal error processing 'Duration' enums");
        }

        T defaultHourlyDuration = durationEnums[0];
        T dailyDuration = durationEnums[1];
        if (dailyDuration.toString().equals(strVal)) {
            return dailyDuration;
        }

        return defaultHourlyDuration;
    }

    private boolean notEqualToAnyEnum(String strVal) {
        return !(strVal.equals(HOURLY.toString()) || strVal.equals(DAILY.toString()));
    }
}
