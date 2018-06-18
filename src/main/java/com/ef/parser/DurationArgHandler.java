package com.ef.parser;

import static com.ef.parser.Duration.DAILY;
import static com.ef.parser.Duration.HOURLY;

public class DurationArgHandler implements ArgHandler<Duration> {

    @Override
    public Duration getValue(String durationStr) {
        Duration duration = DAILY;  // default value
        if (HOURLY.name().toLowerCase().equals(durationStr)) {
            duration = HOURLY;
        }
        return duration;
    }
}
