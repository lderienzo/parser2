/*
 * Created by Luke DeRienzo on 12/12/18 10:59 AM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 12/11/18 6:02 PM
 */

package com.ef.arguments.extraction;


public final class ExtractedArgs {
    private final String accesslog, startDate, duration, threshold;

    private ExtractedArgs(Builder builder) {
        this.accesslog = builder.accesslog;
        this.startDate = builder.startDate;
        this.duration = builder.duration;
        this.threshold = builder.threshold;
    }

    public String getAccesslog() {
        return accesslog;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getDuration() {
        return duration;
    }

    public String getThreshold() {
        return threshold;
    }

    public static final class Builder {
        private String accesslog, startDate, duration, threshold;

        public Builder() {
            accesslog = startDate = duration = threshold = "";
        }

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

        public ExtractedArgs build() {
            return new ExtractedArgs(this);
        }
    }
}
