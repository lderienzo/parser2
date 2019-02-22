/*
 * Created by Luke DeRienzo on 2/5/19 12:24 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 2/5/19 12:23 PM
 */

package com.ef.arguments.enums;

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
