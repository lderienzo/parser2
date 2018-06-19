package com.ef.arguments;

import static com.ef.arguments.Args.ARG_HANDLER_MAP;
import static com.ef.arguments.Args.ArgName.ACCESS_LOG;
import static com.ef.arguments.Args.ArgName.DURATION;
import static com.ef.arguments.Args.ArgName.START_DATE;
import static com.ef.arguments.Args.ArgName.THRESHOLD;
import static com.ef.enums.Duration.DAILY;
import static com.ef.enums.Duration.HOURLY;
import static com.ef.utils.ParserTestUtils.HOURLY_TEST_START_DATE;
import static com.ef.utils.ParserTestUtils.BOGUS_TEST_LOG_FILE_PATH;
import static com.ef.utils.ParserTestUtils.THRESHOLD_200;
import static com.ef.utils.ParserTestUtils.VALID_TEST_LOG_FILE_PATH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import java.time.LocalDateTime;
import java.util.Map;
import org.junit.Test;

import com.ef.enums.Duration;


public class ArgsTest {

    @Test
    public void testProcess_args_map_all_args_present() throws ArgsException {
        String[] params = {
            "--"+ACCESS_LOG+"="+BOGUS_TEST_LOG_FILE_PATH,
            "--"+START_DATE+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION+"="+HOURLY,
            "--"+THRESHOLD+"="+THRESHOLD_200
        };
        Map<String, String> argsMap = Args.getMap(params);
        assertTrue(params.length == argsMap.size());
    }

    @Test
    public void testProcess_args_map_all_args_present_in_different_order() throws ArgsException {
        String[] params = {
            "--"+THRESHOLD+"="+THRESHOLD_200,
            "--"+DURATION+"="+HOURLY,
            "--"+START_DATE+"="+HOURLY_TEST_START_DATE,
            "--"+ACCESS_LOG+"="+BOGUS_TEST_LOG_FILE_PATH
        };
        Map<String, String> argsMap = Args.getMap(params);
        assertTrue(params.length == argsMap.size());
    }

    @Test(expected = ArgsException.class)
    public void testProcess_args_map_all_args_absent() throws ArgsException {
        String[] params = {};
        Map<String, String> argsMap = Args.getMap(params);
        assertTrue(params.length == argsMap.size());
    }

    @Test(expected = ArgsException.class)
    public void testProcess_missing_required_arg() throws ArgsException {
        String[] params = {
            "--"+ACCESS_LOG+"="+BOGUS_TEST_LOG_FILE_PATH,
            "--"+START_DATE+"="+HOURLY_TEST_START_DATE,
            "--"+THRESHOLD+"="+THRESHOLD_200
        };
        Args.getMap(params);
    }

    @Test
    public void testProcess_args_when_access_log_file_path_absent() throws ArgsException {
        String[] params = {
            "--"+START_DATE+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION+"="+HOURLY,
            "--"+THRESHOLD+"="+THRESHOLD_200
        };
        Map<String, String> argsMap = Args.getMap(params);
        assertTrue(params.length == argsMap.size());
    }

    @Test(expected = ArgsException.class)
    public void testProcess_args_when_required_and_non_required_arg_absent() throws ArgsException {
        String[] params = {
            "--"+START_DATE+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION+"="+HOURLY
        };
        Args.getMap(params);
    }

    @Test
    public void testProcess_valid_access_log_path() throws ArgsException {
        String[] params = {
                "--"+ACCESS_LOG+"="+VALID_TEST_LOG_FILE_PATH,
                "--"+START_DATE+"="+HOURLY_TEST_START_DATE,
                "--"+DURATION+"="+HOURLY,
                "--"+THRESHOLD+"="+THRESHOLD_200
        };
        Map<String, String> argsMap = Args.getMap(params);
        String filePath = ARG_HANDLER_MAP.get(ACCESS_LOG)
                .getValue(argsMap.get(ACCESS_LOG.toString()), String.class);
        assertTrue(argsMap.size() == 4);
        assertEquals(VALID_TEST_LOG_FILE_PATH, filePath);
    }

    @Test(expected = ArgsException.class)
    public void testProcess_only_valid_access_log_path_present() throws ArgsException {
        String[] params = {
                "--"+ACCESS_LOG+"="+VALID_TEST_LOG_FILE_PATH
        };
        Args.getMap(params);
    }

    @Test(expected = ArgsException.class)
    public void testProcess_only_invalid_access_log_path_present() throws ArgsException {
        String[] params = {
                "--"+ACCESS_LOG+"="+BOGUS_TEST_LOG_FILE_PATH
        };
        Args.getMap(params);
    }

    @Test
    public void testProcess_invalid_access_log_arg_format_no_equals() throws ArgsException {
        String[] params = {
            "--"+ACCESS_LOG+BOGUS_TEST_LOG_FILE_PATH,
            "--"+START_DATE+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION+"="+HOURLY,
            "--"+THRESHOLD+"="+THRESHOLD_200
        };
        Map<String, String> argsMap = Args.getMap(params);
        ARG_HANDLER_MAP.get(ACCESS_LOG)
                .getValue(argsMap.get(ACCESS_LOG.toString()), String.class);
        assertTrue(argsMap.size() == 3);
    }

    @Test(expected = ArgsException.class)
    public void testProcess_invalid_access_log_path_with_equals() throws ArgsException {
        String[] params = {
            "--"+ACCESS_LOG+"="+BOGUS_TEST_LOG_FILE_PATH,
            "--"+START_DATE+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION+"="+HOURLY,
            "--"+THRESHOLD+"="+THRESHOLD_200
        };
        Map<String, String> argsMap = Args.getMap(params);
        ARG_HANDLER_MAP.get(ACCESS_LOG)
                .getValue(argsMap.get(ACCESS_LOG.toString()), String.class);
    }

    @Test
    public void testProcess_valid_start_date_value() throws ArgsException {
        String[] params = {
            "--"+START_DATE+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION+"="+HOURLY,
            "--"+THRESHOLD+"="+THRESHOLD_200
        };
        Map<String, String> argsMap = Args.getMap(params);
        LocalDateTime startDate = ARG_HANDLER_MAP.get(START_DATE)
                .getValue(argsMap.get(START_DATE.toString()), LocalDateTime.class);
        String expected = new String(HOURLY_TEST_START_DATE).replace(".", "T")
                .subSequence(0,HOURLY_TEST_START_DATE.length()-3).toString();

        assertTrue(argsMap.size() == 3);
        assertEquals(expected, startDate.toString());
    }


    @Test(expected = ArgsException.class)
    public void testProcess_invalid_start_date_value() throws ArgsException {
        String[] params = {
            "--"+START_DATE+"=2017-01-01~~~20:00:00",
            "--"+DURATION+"="+HOURLY,
            "--"+THRESHOLD+"="+THRESHOLD_200
        };
        Map<String, String> argsMap = Args.getMap(params);
        ARG_HANDLER_MAP.get(START_DATE)
                .getValue(argsMap.get(START_DATE.toString()), LocalDateTime.class);
    }

    @Test
    public void testProcess_valid_duration_value_hourly() throws ArgsException {
        String[] params = {
            "--"+START_DATE+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION+"="+HOURLY,
            "--"+THRESHOLD+"="+THRESHOLD_200
        };
        Map<String, String> argsMap = Args.getMap(params);
        Duration duration = ARG_HANDLER_MAP.get(DURATION)
                .getValue(argsMap.get(DURATION.toString()), Duration.class);
        assertTrue(argsMap.size() == 3);
        assertEquals(HOURLY, duration);
    }

    @Test
    public void testProcess_valid_duration_value_daily() throws ArgsException {
        String[] params = {
            "--"+START_DATE+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION+"="+DAILY,
            "--"+THRESHOLD+"="+THRESHOLD_200
        };
        Map<String, String> argsMap = Args.getMap(params);
        Duration duration = ARG_HANDLER_MAP.get(DURATION)
                .getValue(argsMap.get(DURATION.toString()), Duration.class);
        assertTrue(argsMap.size() == 3);
        assertEquals(DAILY, duration);
    }

    @Test(expected = ArgsException.class)
    public void testProcess_invalid_required_arg_value() throws ArgsException {
        String[] params = {
            "--"+START_DATE+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION+"=boguz_value",
            "--"+THRESHOLD+"="+THRESHOLD_200
        };
        Map<String, String> argsMap = Args.getMap(params);
        ARG_HANDLER_MAP.get(DURATION)
                .getValue(argsMap.get(DURATION.toString()), Duration.class);
    }

    @Test(expected = ArgsException.class)
    public void testProcess_missing_required_value_with_equals_char() throws ArgsException {
        String[] params = {
            "--"+START_DATE+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION+"=",
            "--"+THRESHOLD+"="+THRESHOLD_200
        };
        Args.getMap(params);
    }

    @Test(expected = ArgsException.class)
    public void testProcess_missing_required_arg_value_without_equals_char() throws ArgsException {
        String[] params = {
            "--"+START_DATE+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION+"",
            "--"+THRESHOLD+"="+THRESHOLD_200
        };
        Args.getMap(params);
    }

    @Test
    public void testProcess_valid_threshold_value() throws ArgsException {
        String[] params = {
            "--"+START_DATE+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION+"="+DAILY,
            "--"+THRESHOLD+"="+THRESHOLD_200
        };
        Map<String, String> argsMap = Args.getMap(params);
        Integer threshold = ARG_HANDLER_MAP.get(THRESHOLD)
                .getValue(argsMap.get(THRESHOLD.toString()), Integer.class);
        assertTrue(argsMap.size() == 3);
        assertEquals(THRESHOLD_200, threshold.intValue());
    }

    @Test(expected = ArgsException.class)
    public void testProcess_invalid_threshold_value_below_range() throws ArgsException {
        String[] params = {
            "--"+START_DATE+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION+"="+DAILY,
            "--"+THRESHOLD+"=50"
        };
        Map<String, String> argsMap = Args.getMap(params);
        ARG_HANDLER_MAP.get(THRESHOLD)
                .getValue(argsMap.get(THRESHOLD.toString()), Integer.class);
    }

    @Test(expected = ArgsException.class)
    public void testProcess_invalid_threshold_value_above_range() throws ArgsException {
        String[] params = {
            "--"+START_DATE+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION+"="+DAILY,
            "--"+THRESHOLD+"=550"
        };
        Map<String, String> argsMap = Args.getMap(params);
        ARG_HANDLER_MAP.get(THRESHOLD)
                .getValue(argsMap.get(THRESHOLD.toString()), Integer.class);
    }

    @Test(expected = ArgsException.class)
    public void testProcess_invalid_threshold_character_val() throws ArgsException {
        String[] params = {
            "--"+START_DATE+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION+"="+DAILY,
            "--"+THRESHOLD+"=XYZ"
        };
        Map<String, String> argsMap = Args.getMap(params);
        ARG_HANDLER_MAP.get(THRESHOLD)
                .getValue(argsMap.get(THRESHOLD.toString()), Integer.class);
    }

    @Test(expected = ArgsException.class)
    public void testProcess_invalid_threshold_missing_value() throws ArgsException {
        String[] params = {
            "--"+START_DATE+"="+HOURLY_TEST_START_DATE,
            "--"+DURATION+"="+DAILY,
            "--"+THRESHOLD+"="
        };
        Args.getMap(params);
    }
}
