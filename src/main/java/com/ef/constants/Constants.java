/*
 * Created by Luke DeRienzo on 12/23/18 8:26 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 12/23/18 8:26 PM
 */

package com.ef.constants;

import static com.ef.arguments.enums.Args.START_DATE;
import static com.ef.arguments.enums.Args.THRESHOLD;

// TODO: clean up
public final class Constants {
    public static final String STORE_PASSWORD = "password";
    public static final String CONVERTER_ERR_MSG_PREFIX_PREFIX = "Unable to convert ";
    public static final String CONVERTER_ERR_MSG_PREFIX_SUFFIX = "value";
    public static final String OPEN_BRACKET = " [";
    public static final String STRING_TYPE_CONVERTER_VALUE_ABSENT_ERR_MSG =
            CONVERTER_ERR_MSG_PREFIX_PREFIX + CONVERTER_ERR_MSG_PREFIX_SUFFIX + ". Value is null or empty.";
    public static final String START_DATE_CONVERTER_ERR_MSG_PREFIX =
            CONVERTER_ERR_MSG_PREFIX_PREFIX + START_DATE.toString() + CONVERTER_ERR_MSG_PREFIX_SUFFIX + OPEN_BRACKET;
    public static final String START_DATE_CONVERTER_ERR_MSG_SUFFIX = "] to LocalDateTime.";
    public static final String THRESHOLD_CONVERTER_ERR_MSG_PREFIX =
            CONVERTER_ERR_MSG_PREFIX_PREFIX + THRESHOLD.toString() + CONVERTER_ERR_MSG_PREFIX_SUFFIX + OPEN_BRACKET;
    public static final String THRESHOLD_CONVERTER_ERR_MSG_SUFFIX = "] to Integer.";
    public static final String THRESHOLD_CONVERTER_OUTSIDE_ALLOWABLE_RANGE_ERR_MSG = "Threshold outside allowable range.";
    public static final String DURATION_CONVERTER_ERR_MSG = "String value does not represent valid duration value.";
    public static final String INVALID_ACCESS_LOG_PATH_ERR_MSG = "Log file not found.";
    public static final String ARG_EXTRACTOR_DUPLICATE_ARGS_ERR_MSG_PREFIX =
            "Failure in ArgExtractor::throwExceptionIfArgListContainsMoreThanOneValue. Error extracting argument [";
    public static final String ARG_EXTRACTOR_DUPLICATE_ARGS_ERR_MSG_SUFFIX = "]. Single value required. Please see application 'Usage'.";
    public static final String LOG_ENTRY_STORE_ERR_LOADING_LOG_FILE = "Failure in SpeedmentBlockedIpStore::loadFile. Error loading log file into database.";
    public static final String PARSER_BLOCKED_IPS_FOUND_MSG = "Blocked Ips Found With Entered Criteria:";
    public static final String IP_MATCHING_REGEX= "^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
}