package com.ef;

import static com.ef.Duration.DAILY;
import static com.ef.Duration.HOURLY;
import static com.ef.ParserUtils.ARGUMENT_DATE_FORMATTER;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


import com.google.common.base.Strings;

public class DurationArgHandler implements ArgHandler {

    @Override
    public <T> T getValue(String strArgVal, Class<T> clazz) throws ArgsException {

        if (Strings.isNullOrEmpty(strArgVal) || (!strArgVal.equals(HOURLY.toString().toLowerCase())
                && !strArgVal.equals(DAILY.toString().toLowerCase()))) {
            System.out.println("Error: Invalid value entered for 'duration'!");
            System.out.println(Args.getUsage());
            throw new ArgsException("Invalid duration value.");
        }

        T defaultDuration;
        T[] durationEnums = clazz.getEnumConstants();
        if (durationEnums.length == 2) {
            defaultDuration = durationEnums[0];    // default value set to HOURLY
        }
        else {
            throw new ArgsException("Error - internal error processing 'Duration' enums");
        }
        if (durationEnums[1].toString().toLowerCase().equals(strArgVal)) {
            return durationEnums[1];
        }
        return defaultDuration;
    }
}
