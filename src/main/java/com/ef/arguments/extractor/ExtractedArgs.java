/*
 * Created by Luke DeRienzo on 12/10/18 12:49 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 12/9/18 10:35 PM
 */

package com.ef.arguments.extractor;


public final class ExtractedArgs {
    private String accesslog, startDate, duration, threshold;

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
        private String accesslog, startDate, duration, threshold = "";

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

        public ExtractedArgs build() {
            return new ExtractedArgs(this);
        }
    }
}
