package com.ef;

public class IntArgHandler implements ArgHandler<Integer> {

    @Override
    public Integer getValue(String thresholdStr) throws ArgsException {
        int threshold = Integer.parseInt(thresholdStr);
        if (threshold < 100 || threshold > 500) { // set arbitrary lower and upper bounds on threshold
            throw new ArgsException("Threshold must be between 100-500");
        }
        return threshold;
    }
}
