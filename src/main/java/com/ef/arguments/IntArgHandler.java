package com.ef.arguments;

import com.google.common.base.Strings;

public class IntArgHandler implements ArgHandler {

    @Override
    public <T> T getValue(String strVal, Class<T> clazz) throws ArgsException {

        Integer threshold;
        try {
            if (Strings.isNullOrEmpty(strVal)) {
                throw new ArgsException("Error: Threshold value is null or empty!");
            }
            threshold = Integer.parseInt(strVal);

        } catch (NumberFormatException e) {
            System.out.println("Error: Threshold value must be an integer!");
            throw new ArgsException("Invalid threshold argument... non-integer value encountered.", e);
        }

        if (outsideBounds(threshold)) {
            System.out.println("Error: Threshold value must be between 100 and 500!");
            throw new ArgsException("Invalid threshold argument... outside allowed threshold range.");
        }

        return (T)threshold;
    }

    private boolean outsideBounds(int threshold) {
        return (threshold < 100 || threshold > 500);
    }
}
