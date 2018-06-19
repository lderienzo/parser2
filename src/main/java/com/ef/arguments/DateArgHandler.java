package com.ef.arguments;

import static com.ef.utils.ParserUtils.ARGUMENT_DATE_FORMATTER;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.format.DateTimeFormatter;

import com.google.common.base.Strings;


public class DateArgHandler implements ArgHandler {

    @Override
    public <T> T getValue(String strVal, Class<T> clazz) throws ArgsException {
        T returnType;
        try {
            if (Strings.isNullOrEmpty(strVal)) {
                throw new ArgsException("Error: startDate value is null or empty!");
            }
            Method lclDateTimeParse
                    = clazz.getDeclaredMethod("parse", CharSequence.class, DateTimeFormatter.class);
            returnType = (T) lclDateTimeParse.invoke(null, strVal, ARGUMENT_DATE_FORMATTER);
        } catch (NoSuchMethodException|IllegalAccessException|InvocationTargetException e) {
            System.out.println("Error: Invalid format for 'startDate'!");
            throw new ArgsException("Error processing date argument", e);
        }
        return returnType;
    }
}
