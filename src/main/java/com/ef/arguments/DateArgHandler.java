package com.ef.arguments;

import static com.ef.utils.ParserUtils.ARGUMENT_DATE_FORMATTER;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.format.DateTimeFormatter;

import com.google.common.base.Strings;


public class DateArgHandler implements ArgHandler {

    @Override
    public <T> T getValue(String strVal, Class<T> clazz) {
        T returnType;
        try {
            if (Strings.isNullOrEmpty(strVal)) {
                throw new ArgsException("Failure in DateArgHandler::getValue. Value for startDate is null or empty.");
            }
            Method localDateTimeParse
                    = clazz.getDeclaredMethod("parse", CharSequence.class, DateTimeFormatter.class);
            returnType = (T) localDateTimeParse.invoke(null, strVal, ARGUMENT_DATE_FORMATTER);
        } catch (NoSuchMethodException|IllegalAccessException|InvocationTargetException e) {
            throw new ArgsException("Failure in DateArgHandler::getValue. Internal processing error.", e);
        }
        return returnType;
    }
}
