package com.ef.enums;

public enum Duration {
    HOURLY {
        @Override
        public String toString() {
            return "hourly";
        }
    },
    DAILY {
        @Override
        public String toString() {
            return "daily";
        }
    },
}
