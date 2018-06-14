package com.ef.parser;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    private LogReader logReader;

    public Parser() {

    }


    /**
     * This method does it all
     * @param logFile
     * @param startDate
     * @param duration
     * @param threshold
     * @return
     */
    public List<BlockedIP> checkRequests(File logFile, LocalDateTime startDate, Duration duration, int threshold) {
        // parse file
        return new ArrayList<>();
    }
}
