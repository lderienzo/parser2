package com.ef;

public class IntArgHandler implements ArgHandler {

    @Override
    public <T> T getValue(String strArgVal, Class<T> clazz) throws ArgsException {
        Integer threshold;
        try {
            threshold = Integer.parseInt(strArgVal);
        } catch (NumberFormatException e) {
            System.out.println("Error: Threshold value must be an integer!");
            System.out.println(Args.getUsage());
            throw new ArgsException("Invalid threshold argument... non-integer value encountered.", e);
        }
        if (threshold < 100 || threshold > 500) { // set arbitrary lower and upper bounds on threshold
            System.out.println("Error: Threshold value must be between 100 and 500!");
            System.out.println(Args.getUsage());
            throw new ArgsException("Invalid threshold argument... outside allowed threshold range.");
        }
        return (T)threshold;
    }
}
