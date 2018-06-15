package com.ef.parser;

import org.junit.Before;
import org.junit.Test;

import com.ef.parser.db.IpAddressConverter;

public class IpAddressConverterTest {
    private IpAddressConverter converter;

    @Before
    public void setUp() {
        converter = new IpAddressConverter();
    }

    @Test
    public void testConvertFromIpToLong() {
        converter.toLong("192.168.72.173");
    }
}
