package com.ef.parser;

import static com.ef.Args.ARG_PROCESSING_MAP;
import static com.ef.Args.ArgName.ACCESS_LOG;
import static com.ef.Args.ArgName.DURATION;
import static com.ef.Args.ArgName.START_DATE;
import static com.ef.Args.ArgName.THRESHOLD;
import static com.ef.Duration.DAILY;
import static com.ef.Duration.HOURLY;
import static com.ef.parser.ParserTestUtils.HOURLY_TEST_START_DATE;
import static com.ef.parser.ParserTestUtils.BOGUS_TEST_LOG_FILE_PATH;
import static com.ef.parser.ParserTestUtils.THRESHOLD_200;
import static com.ef.parser.ParserTestUtils.VALID_TEST_LOG_FILE_PATH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import java.time.LocalDateTime;
import java.util.Map;
import org.junit.Test;

import com.ef.Args;
import com.ef.ArgsException;
import com.ef.Duration;


public class ArgsTest {

    @Test
    public void testProcess_args_map_all_args_present() {
        String[] params = {
            "--"+ACCESS_LOG.toString()+"="+BOGUS_TEST_LOG_FILE_PATH,
            "--"+START_DATE.toString()+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION.toString()+"="+HOURLY.toString().toLowerCase(),
            "--"+THRESHOLD.toString()+"="+THRESHOLD_200
        };
        Map<String, String> argsMap = Args.process(params);
        assertTrue(params.length == argsMap.size());
    }

    @Test
    public void testProcess_args_map_all_args_present_in_different_order() {
        String[] params = {
            "--"+THRESHOLD.toString()+"="+THRESHOLD_200,
            "--"+DURATION.toString()+"="+HOURLY.toString().toLowerCase(),
            "--"+START_DATE.toString()+"="+HOURLY_TEST_START_DATE,
            "--"+ACCESS_LOG.toString()+"="+BOGUS_TEST_LOG_FILE_PATH
        };
        Map<String, String> argsMap = Args.process(params);
        assertTrue(params.length == argsMap.size());
    }

    @Test
    public void testProcess_args_map_empty_if_missing_required_arg() {
        String[] params = {
            "--"+ACCESS_LOG.toString()+"="+BOGUS_TEST_LOG_FILE_PATH,
            "--"+START_DATE.toString()+"="+HOURLY_TEST_START_DATE,
            "--"+THRESHOLD.toString()+"="+THRESHOLD_200
        };
        Map<String, String> argsMap = Args.process(params);
        assertTrue(argsMap.size() == 0);
    }

    @Test
    public void testProcess_args_when_access_log_file_path_absent() {
        String[] params = {
            "--"+START_DATE.toString()+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION.toString()+"="+HOURLY.toString().toLowerCase(),
            "--"+THRESHOLD.toString()+"="+THRESHOLD_200
        };
        Map<String, String> argsMap = Args.process(params);
        assertTrue(params.length == argsMap.size());
    }

    @Test
    public void testProcess_args_when_required_and_non_required_arg_absent() {
        String[] params = {
            "--"+START_DATE.toString()+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION.toString()+"="+HOURLY.toString().toLowerCase()
        };
        Map<String, String> argsMap = Args.process(params);
        assertTrue(argsMap.size() == 0);
    }

    @Test
    public void testProcess_invalid_access_log_arg_format() throws ArgsException {
        String[] params = {
            "--"+ACCESS_LOG.toString()+BOGUS_TEST_LOG_FILE_PATH,
            "--"+START_DATE.toString()+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION.toString()+"="+HOURLY.toString().toLowerCase(),
            "--"+THRESHOLD.toString()+"="+THRESHOLD_200
        };
        Map<String, String> argsMap = Args.process(params);
        String filePath = ARG_PROCESSING_MAP.get(ACCESS_LOG.toString())
                .getValue(argsMap.get(ACCESS_LOG.toString()), String.class);
        assertTrue(argsMap.size() == 3);
        assertTrue(filePath.isEmpty());
    }

    @Test
    public void testProcess_valid_access_log_path() throws ArgsException {
        String[] params = {
            "--"+ACCESS_LOG.toString()+"="+VALID_TEST_LOG_FILE_PATH,
            "--"+START_DATE.toString()+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION.toString()+"="+HOURLY.toString().toLowerCase(),
            "--"+THRESHOLD.toString()+"="+THRESHOLD_200
        };
        Map<String, String> argsMap = Args.process(params);
        String filePath = ARG_PROCESSING_MAP.get(ACCESS_LOG.toString())
                .getValue(argsMap.get(ACCESS_LOG.toString()), String.class);
        assertTrue(argsMap.size() == 4);
        assertEquals(VALID_TEST_LOG_FILE_PATH, filePath);
    }

    @Test(expected = ArgsException.class)
    public void testProcess_invalid_access_log_path() throws ArgsException {
        String[] params = {
            "--"+ACCESS_LOG.toString()+"="+BOGUS_TEST_LOG_FILE_PATH,
            "--"+START_DATE.toString()+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION.toString()+"="+HOURLY.toString().toLowerCase(),
            "--"+THRESHOLD.toString()+"="+THRESHOLD_200
        };
        Map<String, String> argsMap = Args.process(params);
        ARG_PROCESSING_MAP.get(ACCESS_LOG.toString())
                .getValue(argsMap.get(ACCESS_LOG.toString()), String.class);
    }

    @Test
    public void testProcess_valid_start_date_value() throws ArgsException {
        String[] params = {
            "--"+START_DATE.toString()+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION.toString()+"="+HOURLY.toString().toLowerCase(),
            "--"+THRESHOLD.toString()+"="+THRESHOLD_200
        };
        Map<String, String> argsMap = Args.process(params);
        LocalDateTime startDate = ARG_PROCESSING_MAP.get(START_DATE.toString())
                .getValue(argsMap.get(START_DATE.toString()), LocalDateTime.class);
        String expected = new String(HOURLY_TEST_START_DATE).replace(".", "T")
                .subSequence(0,HOURLY_TEST_START_DATE.length()-3).toString();

        assertTrue(argsMap.size() == 3);
        assertEquals(expected, startDate.toString());
    }


    @Test(expected = ArgsException.class)
    public void testProcess_invalid_start_date_value() throws ArgsException {
        String[] params = {
            "--"+START_DATE.toString()+"=2017-01-01~~~20:00:00",
            "--"+DURATION.toString()+"="+HOURLY.toString().toLowerCase(),
            "--"+THRESHOLD.toString()+"="+THRESHOLD_200
        };
        Map<String, String> argsMap = Args.process(params);
        ARG_PROCESSING_MAP.get(START_DATE.toString())
                .getValue(argsMap.get(START_DATE.toString()), LocalDateTime.class);
    }

    @Test
    public void testProcess_valid_duration_value_hourly() throws ArgsException {
        String[] params = {
            "--"+START_DATE.toString()+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION.toString()+"="+HOURLY.toString().toLowerCase(),
            "--"+THRESHOLD.toString()+"="+THRESHOLD_200
        };
        Map<String, String> argsMap = Args.process(params);
        Duration duration = ARG_PROCESSING_MAP.get(DURATION.toString())
                .getValue(argsMap.get(DURATION.toString()), Duration.class);
        assertTrue(argsMap.size() == 3);
        assertEquals(HOURLY, duration);
    }

    @Test
    public void testProcess_valid_duration_value_daily() throws ArgsException {
        String[] params = {
            "--"+START_DATE.toString()+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION.toString()+"="+DAILY.toString().toLowerCase(),
            "--"+THRESHOLD.toString()+"="+THRESHOLD_200
        };
        Map<String, String> argsMap = Args.process(params);
        Duration duration = ARG_PROCESSING_MAP.get(DURATION.toString())
                .getValue(argsMap.get(DURATION.toString()), Duration.class);
        assertTrue(argsMap.size() == 3);
        assertEquals(DAILY, duration);
    }

    @Test(expected = ArgsException.class)
    public void testProcess_invalid_duration_value() throws ArgsException {
        String[] params = {
            "--"+START_DATE.toString()+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION.toString()+"=boguz_value",
            "--"+THRESHOLD.toString()+"="+THRESHOLD_200
        };
        Map<String, String> argsMap = Args.process(params);
        ARG_PROCESSING_MAP.get(DURATION.toString())
                .getValue(argsMap.get(DURATION.toString()), Duration.class);
    }

    @Test(expected = ArgsException.class)
    public void testProcess_missing_duration_value_with_equals_char() throws ArgsException {
        String[] params = {
            "--"+START_DATE.toString()+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION.toString()+"=",
            "--"+THRESHOLD.toString()+"="+THRESHOLD_200
        };
        Map<String, String> argsMap = Args.process(params);
        ARG_PROCESSING_MAP.get(DURATION.toString())
                .getValue(argsMap.get(DURATION.toString()), Duration.class);
    }

    @Test(expected = ArgsException.class)
    public void testProcess_missing_duration_value_without_equals_char() throws ArgsException {
        String[] params = {
            "--"+START_DATE.toString()+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION.toString()+"",
            "--"+THRESHOLD.toString()+"="+THRESHOLD_200
        };
        Map<String, String> argsMap = Args.process(params);
        ARG_PROCESSING_MAP.get(DURATION.toString())
                .getValue(argsMap.get(DURATION.toString()), Duration.class);
    }

    @Test
    public void testProcess_valid_threshold_value() throws ArgsException {
        String[] params = {
            "--"+START_DATE.toString()+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION.toString()+"="+DAILY.toString().toLowerCase(),
            "--"+THRESHOLD.toString()+"="+THRESHOLD_200
        };
        Map<String, String> argsMap = Args.process(params);
        Integer threshold = ARG_PROCESSING_MAP.get(THRESHOLD.toString())
                .getValue(argsMap.get(THRESHOLD.toString()), Integer.class);
        assertTrue(argsMap.size() == 3);
        assertEquals(THRESHOLD_200, threshold.intValue());
    }

    @Test(expected = ArgsException.class)
    public void testProcess_invalid_threshold_value_below_range() throws ArgsException {
        String[] params = {
            "--"+START_DATE.toString()+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION.toString()+"="+DAILY.toString().toLowerCase(),
            "--"+THRESHOLD.toString()+"=50"
        };
        Map<String, String> argsMap = Args.process(params);
        ARG_PROCESSING_MAP.get(THRESHOLD.toString())
                .getValue(argsMap.get(THRESHOLD.toString()), Integer.class);
    }

    @Test(expected = ArgsException.class)
    public void testProcess_invalid_threshold_value_above_range() throws ArgsException {
        String[] params = {
            "--"+START_DATE.toString()+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION.toString()+"="+DAILY.toString().toLowerCase(),
            "--"+THRESHOLD.toString()+"=550"
        };
        Map<String, String> argsMap = Args.process(params);
        ARG_PROCESSING_MAP.get(THRESHOLD.toString())
                .getValue(argsMap.get(THRESHOLD.toString()), Integer.class);
    }

    @Test(expected = ArgsException.class)
    public void testProcess_invalid_threshold_character_val() throws ArgsException {
        String[] params = {
            "--"+START_DATE.toString()+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION.toString()+"="+DAILY.toString().toLowerCase(),
            "--"+THRESHOLD.toString()+"=XYZ"
        };
        Map<String, String> argsMap = Args.process(params);
        ARG_PROCESSING_MAP.get(THRESHOLD.toString())
                .getValue(argsMap.get(THRESHOLD.toString()), Integer.class);
    }

    @Test(expected = ArgsException.class)
    public void testProcess_invalid_threshold_missing_value() throws ArgsException {
        String[] params = {
            "--"+START_DATE.toString()+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION.toString()+"="+DAILY.toString().toLowerCase(),
            "--"+THRESHOLD.toString()+"="
        };
        Map<String, String> argsMap = Args.process(params);
        ARG_PROCESSING_MAP.get(THRESHOLD.toString())
                .getValue(argsMap.get(THRESHOLD.toString()), Integer.class);

    }
}
