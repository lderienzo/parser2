package com.ef.parser;

import static org.junit.Assert.assertEquals;

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
        assertEquals(3232254125L, converter.toLong("192.168.72.173"));
    }

    @Test
    public void testConvertFromLongToIp() {
        assertEquals("192.168.72.173", converter.toIp(3232254125L));
    }
}
