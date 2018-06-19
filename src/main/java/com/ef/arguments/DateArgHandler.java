package com.ef.arguments;

import static com.ef.utils.ParserUtils.ARGUMENT_DATE_FORMATTER;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.format.DateTimeFormatter;


public class DateArgHandler implements ArgHandler {

    @Override
    public <T> T getValue(String dateStr, Class<T> clazz) throws ArgsException {
        T returnType;
        try {
            Method lclDateTimeParse
                    = clazz.getDeclaredMethod("parse", CharSequence.class, DateTimeFormatter.class);
            returnType = (T)lclDateTimeParse.invoke(null, dateStr, ARGUMENT_DATE_FORMATTER);
        } catch (NoSuchMethodException|IllegalAccessException|InvocationTargetException e) {
            System.out.println("Error: Invalid format for 'startDate'!");
            System.out.println(Args.getUsage());
            throw new ArgsException("Error processing date argument", e);
        }
        return returnType;
    }
}
