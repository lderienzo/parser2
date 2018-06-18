package com.ef.parser;

import static com.ef.parser.Args.ArgName.ACCESS_LOG;
import static com.ef.parser.Args.ArgName.DURATION;
import static com.ef.parser.Args.ArgName.START_DATE;
import static com.ef.parser.Args.ArgName.THRESHOLD;
import static org.junit.Assert.assertTrue;


import java.util.Map;
import org.junit.Test;


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
        assertTrue((params.length - 1) == argsMap.size());
    }

}
