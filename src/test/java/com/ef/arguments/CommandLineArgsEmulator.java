/*
 * Created by Luke DeRienzo on 11/6/18 1:00 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 11/5/18 11:03 PM
 */

package com.ef.arguments;

import static com.ef.arguments.enums.Args.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ef.arguments.enums.Args;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;

public final class CommandLineArgsEmulator {

    private boolean leaveOutEqualsSign;
    private final String accesslog, startDate, duration, threshold;
    private final List<String> emulatedArgs = new ArrayList<>(4);
    private static final String EXCEPTION_MESSAGE_PREFIX = "Error accessing class field: ";
    private static final ImmutableMap<String, Args> STRING_TO_ENUM_ARG_MAP =
            new ImmutableMap.Builder<String, Args>()
                    .put(ACCESS_LOG.toString(), ACCESS_LOG)
                    .put(START_DATE.toString(), START_DATE)
                    .put(DURATION.toString(), DURATION)
                    .put(THRESHOLD.toString(), THRESHOLD)
                    .build();


    private CommandLineArgsEmulator(Builder builder) {
        this.accesslog = builder.accesslog;
        this.startDate = builder.startDate;
        this.duration = builder.duration;
        this.threshold = builder.threshold;
        this.leaveOutEqualsSign = builder.leaveOutEqualsSign;
    }

    public static final class Builder {
        private String accesslog = "";
        private String startDate = "";
        private String duration = "";
        private String threshold = "";
        private boolean leaveOutEqualsSign = false;

        public Builder() {}

        public Builder accesslog(String val) {
            accesslog = val;
            return this;
        }

        public Builder startDate(String val) {
            startDate = val;
            return this;
        }

        public Builder duration(String val) {
            duration = val;
            return this;
        }

        public Builder threshold(String val) {
            threshold = val;
            return this;
        }

        public Builder leaveOutEqualsSign(boolean val) {
            leaveOutEqualsSign = val;
            return this;
        }

        public CommandLineArgsEmulator build() {
            return new CommandLineArgsEmulator(this);
        }
    }

    public final String[] getEmulatedArgsArray() {
        addArgumentMembersContainingValuesToEmulatedArgs();
        return emulatedArgs.toArray(new String[0]);
    }

    private void addArgumentMembersContainingValuesToEmulatedArgs() {
        Arrays.stream(this.getClass().getDeclaredFields())
                .filter(this::fieldIsArgument)
                .filter(this::argumentFieldContainsValue)
                .forEach(this::addFieldValue);
    }

    private boolean fieldIsArgument(Field field) {
        return fieldIsNotAnyOfThese(field);
    }

    private boolean fieldIsNotAnyOfThese(Field field) {
        return !(field.getName().equals("leaveOutEqualsSign") ||
                field.getName().equals("extraBogusArg") ||
                field.getName().equals("STRING_TO_ENUM_ARG_MAP") ||
                field.getName().equals("EXCEPTION_MESSAGE_PREFIX") ||
                field.getName().equals("emulatedArgs"));
    }

    private boolean argumentFieldContainsValue(Field field) {
        boolean containsArgValue;
        try {
            containsArgValue = !Strings.isNullOrEmpty(field.get(this).toString());
        } catch (IllegalAccessException e) {
            throw new CommandLineArgsEmulatorException(EXCEPTION_MESSAGE_PREFIX + field.getName(), e);
        }
        return containsArgValue;
    }

    private void addFieldValue(Field field) {
        try {
            emulatedArgs.add(commandArgStringForField(field));
        } catch (IllegalAccessException e) {
            throw new CommandLineArgsEmulatorException(EXCEPTION_MESSAGE_PREFIX + field.getName(), e);
        }
        turnOffLeaveOutEqualsSignFlag();
    }

    private String commandArgStringForField(Field field) throws IllegalAccessException {
        return new StringBuilder()
            .append("--")
            .append(STRING_TO_ENUM_ARG_MAP.get(field.getName()))
            .append((leaveOutEqualsSign ? "" : "="))
            .append(field.get(this)).toString();
    }

    private void turnOffLeaveOutEqualsSignFlag() {
        if (leaveOutEqualsSign) {
            leaveOutEqualsSign = false;
        }
    }
}
