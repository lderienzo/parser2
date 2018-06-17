package com.ef.parser;


import java.time.LocalDateTime;
import java.util.Map;

public interface LogReader {

    /**
     * Reads in log file and stores each row in the database.
     * @param path
     */
    void read(String path);

    /**
     * Determines which ips present in the access log should be blocked based on the
     * the values of the input parameters which are obtained from the command line.
     * @param startDate
     * @param duration
     * @param threshold
     * @return
     */
    Map<Long, Long> getBlockedIps(LocalDateTime startDate, String duration, int threshold);

    /**
     * @param blockedIps - Map of blocked IP addresses with IP address as key and
     *                   number of requests resulting in the blocked status.
     */
    void printBlockedIpsToConsole(Map<Long, Long> blockedIps);
}
