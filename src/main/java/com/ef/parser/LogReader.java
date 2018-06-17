package com.ef.parser;


import java.time.LocalDateTime;
import java.util.Map;

public interface LogReader {

    /**
     * Reads entire log file into the database.
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

    /** Prints list of blocked IPs to console.
     * @param blockedIps - Map of blocked IP addresses with IP address as key and
     *                   number of requests resulting in the blocked status.
     */
    void printBlockedIps(Map<Long, Long> blockedIps);

    void saveBlockedIps(Map<Long, Long> blockedIps);
}
