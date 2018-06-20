package com.ef.arguments;

import static com.ef.enums.Duration.DAILY;
import static com.ef.enums.Duration.HOURLY;


import com.google.common.base.Strings;

public class DurationArgHandler implements ArgHandler {

    @Override
    public <T> T getValue(String strVal, Class<T> clazz) throws ArgsException {

        T dailyDuration;
        T hourlyDuration;
        T[] durationEnums = clazz.getEnumConstants();
        if (durationEnums.length == 2 &&
                durationEnums[0].equals(HOURLY) && durationEnums[1].equals(DAILY)) {

            hourlyDuration = durationEnums[0];
            dailyDuration = durationEnums[1];
        }
        else {
            throw new ArgsException("Error - error processing 'Duration' enums");
        }

        if (isNonEmpty(strVal) && strVal.equals(dailyDuration.toString())) {
            return dailyDuration;
        }
        else if (isNonEmpty(strVal) && strVal.equals(hourlyDuration.toString())) {
            return hourlyDuration;
        }
        else {
            System.out.println("Error: Invalid value entered for duration.");
            throw new ArgsException("Invalid duration value.");
        }
    }

    private boolean isNonEmpty(String strVal) {
        return !Strings.isNullOrEmpty(strVal);
    }
}
