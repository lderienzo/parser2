package com.ef.parser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ParserTest {

    private Parser parser;
    private File logFile;
    private LocalDateTime startDate;
    private Duration duration;
    private int threshold;

    @Before
    public void setUp() {
        parser = new Parser();
        // read file contents somehow
        logFile = new File("");
        String str = "2017-01-01 23:59:32.133";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        startDate = LocalDateTime.parse(str, formatter);
        duration = Duration.HOURLY;
        threshold = 100;
    }

    @Test
    public void testCheckRequests_zero_blocked_requests_produces_empty_list() {
        List<BlockedIP> flaggedIps = parser.checkRequests(logFile, startDate, duration, threshold);
        assertEquals(0, flaggedIps.size());
    }

    @Test
    @Ignore
    public void testCheckRequests_one_blocked_request_produces_list_of_size_one() {
        List<BlockedIP> flaggedIps = parser.checkRequests(logFile, startDate, duration, threshold);
        assertEquals(1, flaggedIps.size());
    }

    @Test
    @Ignore
    public void testCheckRequests_n_blocked_requests_produces_list_of_size_n() {
        List<BlockedIP> flaggedIps = parser.checkRequests(logFile, startDate, duration, threshold);
        assertEquals(5, flaggedIps.size());
    }

}
