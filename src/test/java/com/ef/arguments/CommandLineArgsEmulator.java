/*
 * Created by Luke DeRienzo on 11/6/18 1:00 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 11/5/18 11:03 PM
 */

package com.ef.arguments;

import static com.ef.arguments.Args.ArgName.ACCESS_LOG;
import static com.ef.arguments.Args.ArgName.DURATION;
import static com.ef.arguments.Args.ArgName.START_DATE;
import static com.ef.arguments.Args.ArgName.THRESHOLD;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;

final class CommandLineArgsEmulator {
    private String accesslog;
    private String startDate;
    private String duration;
    private String threshold;
    private String extraBogusArg;
    private boolean leaveOutEqualsSign;
    private List<String> emulatedArgs = new ArrayList<>(4);
    private static final String EXCEPTION_MESSAGE_PREFIX = "Error accessing class field: ";
    private static final ImmutableMap<String, Args.ArgName> STRING_TO_ENUM_ARG_MAP =
            new ImmutableMap.Builder<String, Args.ArgName>()
                    .put("accesslog", ACCESS_LOG)
                    .put("startDate", START_DATE)
                    .put("duration", DURATION)
                    .put("threshold", THRESHOLD)
                    .build();

    private CommandLineArgsEmulator(Builder builder) {
        accesslog = builder.accesslog;
        startDate = builder.startDate;
        duration = builder.duration;
        threshold = builder.threshold;
        extraBogusArg = builder.extraBogusArg;
        leaveOutEqualsSign = builder.leaveOutEqualsSign;
    }

    static final class Builder {
        private String accesslog = "";
        private String startDate = "";
        private String duration = "";
        private String threshold = "";
        private String extraBogusArg = "";
        private boolean leaveOutEqualsSign = false;

        public Builder() {}

        public Builder pathToLogFile(String val) {
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

        public Builder extraBogusArg(String val) {
            extraBogusArg = val;
            return this;
        }

        public CommandLineArgsEmulator build() {
            return new CommandLineArgsEmulator(this);
        }
    }

    final String[] getEmulatedArgsArray() {
        Arrays.stream(this.getClass().getDeclaredFields())
            .filter(this::fieldIsAnArgName)
            .filter(this::fieldContainsAnArgValue)
            .forEach(field -> addFieldValue(field));
        return emulatedArgs.stream().toArray(String[]::new);
    }

    private boolean fieldIsAnArgName(Field field) {
        return !(field.getName().equals("leaveOutEqualsSign") ||
                field.getName().equals("extraBogusArg") ||
                field.getName().equals("STRING_TO_ENUM_ARG_MAP") ||
                field.getName().equals("EXCEPTION_MESSAGE_PREFIX") ||
                field.getName().equals("emulatedArgs"));
    }

    private boolean fieldContainsAnArgValue(Field field) {
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
            emulatedArgs.add(
                new StringBuilder()
                    .append("--")
                    .append(STRING_TO_ENUM_ARG_MAP.get(field.getName()))
                    .append((leaveOutEqualsSign ? "" : "="))
                    .append(field.get(this))
                    .toString());
        } catch (IllegalAccessException e) {
            throw new CommandLineArgsEmulatorException(EXCEPTION_MESSAGE_PREFIX + field.getName(), e);
        }
        if (leaveOutEqualsSign) {
            leaveOutEqualsSign = false;
        }
    }

}
