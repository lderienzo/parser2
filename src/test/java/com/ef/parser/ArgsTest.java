package com.ef.parser;

import static com.ef.Args.ARG_PROCESSING_MAP;
import static com.ef.Args.ArgName.ACCESS_LOG;
import static com.ef.Args.ArgName.DURATION;
import static com.ef.Args.ArgName.START_DATE;
import static com.ef.Args.ArgName.THRESHOLD;
import static org.junit.Assert.assertTrue;


import java.util.Map;
import org.junit.Test;

import com.ef.ArgHandler;
import com.ef.Args;
import com.ef.ArgsException;


public class ArgsTest {

    @Test
    public void testProcess_args_map_all_args_present() {
        String[] params = {
            "--"+ACCESS_LOG.toString()+"=/path/hourly_test_access.log",
            "--"+START_DATE.toString()+"=2017-01-01.20:00:00",
            "--"+DURATION.toString()+"=hourly",
            "--"+THRESHOLD.toString()+"=200"
        };
        Map<String, String> argsMap = Args.process(params);
        assertTrue(params.length == argsMap.size());
    }

    @Test
    public void testProcess_args_map_all_args_present_in_different_order() {
        String[] params = {
                "--"+THRESHOLD.toString()+"=200",
                "--"+DURATION.toString()+"=hourly",
                "--"+START_DATE.toString()+"=2017-01-01.20:00:00",
                "--"+ACCESS_LOG.toString()+"=/path/hourly_test_access.log"

        };
        Map<String, String> argsMap = Args.process(params);
        assertTrue(params.length == argsMap.size());
    }

    @Test
    public void testProcess_args_map_empty_if_missing_required_arg_duration() {
        String[] params = {
            "--"+ACCESS_LOG.toString()+"=/path/hourly_test_access.log",
            "--"+START_DATE.toString()+"=2017-01-01.20:00:00",
            "--"+THRESHOLD.toString()+"=200"
        };
        Map<String, String> argsMap = Args.process(params);
        assertTrue(argsMap.size() == 0);
    }


    @Test
    public void testProcess_args_when_access_log_file_path_absent() {
        String[] params = {
                "--"+START_DATE.toString()+"=2017-01-01.20:00:00",
                "--"+DURATION.toString()+"=hourly",
                "--"+THRESHOLD.toString()+"=200"
        };
        Map<String, String> argsMap = Args.process(params);
        assertTrue(params.length == argsMap.size());
    }

    @Test
    public void testProcess_args_when_required_and_non_required_arg_absent() {
        String[] params = {
                "--"+START_DATE.toString()+"=2017-01-01.20:00:00",
                "--"+DURATION.toString()+"=hourly",
        };
        Map<String, String> argsMap = Args.process(params);
        assertTrue(argsMap.size() == 0);
    }

    @Test // currently failing
    public void testProcess_invalid_empty_access_log_value() throws ArgsException {
        String[] params = {
                "--"+ACCESS_LOG.toString()+"blah",
                "--"+START_DATE.toString()+"=2017-01-01.20:00:00",
                "--"+DURATION.toString()+"=hourly",
                "--"+THRESHOLD.toString()+"=200"
        };
        Map<String, String> argsMap = Args.process(params);
        ArgHandler<String> pathArgHandler = ARG_PROCESSING_MAP.get(ACCESS_LOG.toString());
        String filePath = pathArgHandler.getValue(argsMap.get(ACCESS_LOG.toString()));
        assertTrue(argsMap.size() == 0);
    }

}
