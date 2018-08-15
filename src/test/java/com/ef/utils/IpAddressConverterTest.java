/*
 * Created by Luke DeRienzo on 8/15/18 12:29 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 6/18/18 10:46 PM
 */

package com.ef.utils;

import static com.ef.utils.ParserTestUtils.HOURLY_TEST_IP;
import static com.ef.utils.ParserTestUtils.HOURLY_TEST_IP_LONG;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class IpAddressConverterTest {
    private IpAddressConverter converter;

    @Before
    public void setUp() {
        converter = new IpAddressConverter();
    }

    @Test
    public void testConvertFromIpToLong() {
        assertEquals(HOURLY_TEST_IP_LONG, converter.toLong(HOURLY_TEST_IP));
    }

    @Test
    public void testConvertFromLongToIp() {
        assertEquals(HOURLY_TEST_IP, converter.toIp(HOURLY_TEST_IP_LONG));
    }
}
