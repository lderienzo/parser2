package com.ef.parser.db;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IpAddressConverter {

    public static long toLong(String ip) {
        List<Integer> octets =  Stream.of(ip).map(string -> string.split("\\."))
                .flatMap(Arrays::stream)
                .map(string -> Integer.parseInt(string))
                .collect(Collectors.toList());
//                .forEach(System.out::println);

        long result = 0;
        for (int octetPosition = 0; octetPosition < octets.size(); octetPosition++) {
            int power = octets.size() - (octetPosition + 1);
            result += octets.get(octetPosition) * Math.pow(256, power);
        }

        return result;
    }
}
