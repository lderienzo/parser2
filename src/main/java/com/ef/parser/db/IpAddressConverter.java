package com.ef.parser.db;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IpAddressConverter {

    public long toLong(String ip) {
        Stream.of(ip).map(string -> string.split("\\."))
                .flatMap(Arrays::stream)
                .collect(Collectors.toList())
                .forEach(System.out::println);
        return 0;
    }
}
