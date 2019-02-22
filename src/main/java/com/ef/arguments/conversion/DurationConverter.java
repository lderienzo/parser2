/*
 * Created by Luke DeRienzo on 12/19/18 11:22 AM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 12/18/18 11:16 PM
 */

package com.ef.arguments.conversion;

import static com.ef.constants.Constants.DURATION_CONVERTER_ERR_MSG;
import static com.ef.arguments.enums.Duration.DAILY;
import static com.ef.arguments.enums.Duration.HOURLY;

import com.ef.arguments.ArgsException;
import com.ef.arguments.enums.Duration;

public class DurationConverter implements StringTypeConverter<Duration> {
    private Duration convertedValue;

    @Override
    public Duration convert(String valueName, String value) {
        makeSureValueIsPresent(valueName, value);
        makeSureValueRepresentsDurationEnum(value);
        defaultValueToHourlyDuration();
        if (valueRepresentsDailyDuration(value))
            convertedValue = DAILY;
        return convertedValue;
    }

    private void makeSureValueRepresentsDurationEnum(String value) {
        if (valueDoesNotRepresentDurationValue(value))
            throw new ArgsException(DURATION_CONVERTER_ERR_MSG);
    }

    private boolean valueDoesNotRepresentDurationValue(String value) {
        return !valueRepresentsDurationValue(value);
    }

    private boolean valueRepresentsDurationValue(String value) {
        return (valueRepresentsHourlyDuration(value) || valueRepresentsDailyDuration(value));
    }

    private boolean valueRepresentsHourlyDuration(String value) {
        return value.equals(HOURLY.toString());
    }

    private boolean valueRepresentsDailyDuration(String value) {
        return value.equals(DAILY.toString());
    }

    private void defaultValueToHourlyDuration() {
        convertedValue = HOURLY;
    }
}
