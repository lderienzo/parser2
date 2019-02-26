/*
 * Created by Luke DeRienzo on 2/25/19 9:34 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 2/24/19 12:16 PM
 */

package com.ef.utils;

import static com.ef.utils.IpAddressConverter.*;
import static com.ef.constants.TestConstants.HOURLY_TEST_IP;
import static com.ef.constants.TestConstants.HOURLY_TEST_IP_LONG;
import static org.junit.Assert.assertEquals;


import org.junit.Test;

public class IpAddressConverterTest {
    @Test
    public void testConvertFromIpToLong() {
        assertEquals(HOURLY_TEST_IP_LONG, fromStringToLong(HOURLY_TEST_IP));
    }

    @Test
    public void testConvertFromLongToIp() {
        assertEquals(HOURLY_TEST_IP, fromLongToString(HOURLY_TEST_IP_LONG));
    }
}
