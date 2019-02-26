/*
 * Created by Luke DeRienzo on 2/25/19 9:34 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 2/5/19 12:28 PM
 */

package com.ef.blockedipstore;


import java.time.LocalDateTime;

import com.ef.arguments.enums.Duration;

public final class SearchCriteria {
    private final LocalDateTime startDate;
    private final Duration duration;
    private final int threshold;

    public SearchCriteria(LocalDateTime startDate, Duration duration, int threshold) {
        this.startDate = startDate;
        this.duration = duration;
        this.threshold = threshold;
    }

    public LocalDateTime startDate() {
        return startDate;
    }

    public Duration duration() {
        return duration;
    }

    public int threshold() {
        return threshold;
    }
}
