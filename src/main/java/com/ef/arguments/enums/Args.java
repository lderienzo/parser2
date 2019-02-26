/*
 * Created by Luke DeRienzo on 2/25/19 9:34 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 2/5/19 12:32 PM
 */

package com.ef.arguments.enums;


public enum Args {
    ACCESS_LOG {
        @Override
        public String toString() {
            return "accesslog";
        }
    },
    START_DATE {
        @Override
        public String toString() {
            return "startDate";
        }
    },
    DURATION {
        @Override
        public String toString() {
            return "duration";
        }
    },
    THRESHOLD {
        @Override
        public String toString() {
            return "threshold";
        }
    }
}
