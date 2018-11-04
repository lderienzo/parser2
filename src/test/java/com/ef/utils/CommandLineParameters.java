/*
 * Created by Luke DeRienzo on 11/4/18 10:00 AM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 11/3/18 9:09 PM
 */

package com.ef.utils;

import com.ef.arguments.Args;

public final class CommandLineParameters {
    private String startDate;
    private String duration;
    private String threshold;

    public static final class Builder {
        private final String startDate;
        private String duration;
        private String threshold;

        public Builder(String startDate) {
            this.startDate = startDate;
        }

        public Builder duration(String duration) {
            this.duration = duration;
            return this;
        }

        public Builder threshold(String threshold) {
            this.threshold = threshold;
            return this;
        }

        public String[] build() {
            return new CommandLineParameters().createInstance(this);
        }

    }

    private String[] createInstance(Builder builder) {

        startDate = builder.startDate;
        duration = builder.duration;
        threshold = builder.threshold;

        String[] params = {
            new StringBuilder("--").append(Args.ArgName.START_DATE)
                    .append("=").append(startDate).toString(),
            new StringBuilder("--").append(Args.ArgName.DURATION)
                    .append("=").append(duration).toString(),
            new StringBuilder("--").append(Args.ArgName.THRESHOLD)
                    .append("=").append(threshold).toString()
        };

        return params;
    }


}
