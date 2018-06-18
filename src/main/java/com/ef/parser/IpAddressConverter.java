package com.ef.parser;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IpAddressConverter {

    public static long toLong(String ip) {
        List<Integer> octets =
                Stream.of(ip).map(string -> string.split("\\."))
                    .flatMap(Arrays::stream)
                    .map(string -> Integer.parseInt(string))
                    .collect(Collectors.toList());

        long result = 0;
        for (int octetPosition = 0; octetPosition < octets.size(); octetPosition++) {
            int power = octets.size() - (octetPosition + 1);
            result += octets.get(octetPosition) * Math.pow(256, power);
        }
        return result;
    }

    public static String toIp(Long longIp) {
        return ((longIp >> 24) & 0xFF) + "."
                + ((longIp >> 16) & 0xFF) + "."
                + ((longIp >> 8) & 0xFF) + "."
                + (longIp & 0xFF);
    }
}
