package com.ef.loghandler;


import java.time.LocalDateTime;
import java.util.Map;

import com.ef.enums.Duration;

/**
 * API used to handle all log-processing related functions.
 */
public interface LogHandler {   // TODO: perhaps change name to include something to do with blocking ips?

    /**
     * Reads entire log file into the database.
     * @param path
     */
    void save(String path);

    /**
     * Saves a list of blocked IPs to the database with a comment containing the
     * criteria used to determine the reason for blocking.
     *
     * @param blockedIps
     * @param startDate
     * @param duration
     * @param threshold
     * @return integer value indicating operation status: 1=success, 0=no-op nothing to save
     */
    int saveBlockedIps(Map<Long, Long> blockedIps, LocalDateTime startDate, Duration duration, int threshold);

    /**
     * Determines which ips present in the access log should be blocked based on the
     * the values of the input parameters which are obtained from the command line.
     * @param startDate
     * @param duration
     * @param threshold
     * @return map containing Long representation of ip as the key and the number of
     * occurrences of that ip above the threshold value occurring with the given time frame
     * as the value.
     */
    Map<Long, Long> getBlockedIps(LocalDateTime startDate, Duration duration, int threshold);

    /**
     * Create a string of blocked IPs.
     * @param blockedIps Map of blocked IP addresses with IP address as key and
     *                   number of requests resulting in the blocked status.
     * @return string containing either the list of blocked ips or a message indicating
     * there are non.
     */
    String getBlockedIpsMessage(Map<Long, Long> blockedIps);
}
