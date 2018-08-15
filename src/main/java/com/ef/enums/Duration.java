/*
 * Created by Luke DeRienzo on 8/15/18 12:29 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 6/19/18 4:20 PM
 */

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
