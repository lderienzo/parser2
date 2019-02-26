/*
 * Created by Luke DeRienzo on 2/25/19 9:34 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 2/25/19 5:51 PM
 */

package com.ef.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class IpAddressConverter {
    private static final int[] SHIFT_BITS_RIGHT = {24, 16, 8, 0};
    private static final int HEX_BITWISE_AND_MASK = 0xFF;

    public static long fromStringToLong(String ip) {
        List<Integer> octets = convertIpStringToListOfIntegerOctets(ip);
        return calculateLongIpRepresentationFromOctets(octets);
    }

    private static List<Integer> convertIpStringToListOfIntegerOctets(String ip) {
        return Stream.of(ip).map(string -> string.split("\\."))
                .flatMap(Arrays::stream)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private static long calculateLongIpRepresentationFromOctets(List<Integer> octets) {
        long result = 0;
        for (int octetPosition = 0; octetPosition < octets.size(); octetPosition++) {
            int power = calculatePowerUsingOctetsSizeAndPosition(octets, octetPosition);
            result += multiplyOctetAtCurrentPositionBy256ToTheCalculatedPower(octets, octetPosition, power);
        }
        return result;
    }

    private static int calculatePowerUsingOctetsSizeAndPosition(List<Integer> octets, int octetPosition) {
        return octets.size() - (octetPosition + 1);
    }

    private static double multiplyOctetAtCurrentPositionBy256ToTheCalculatedPower(
            List<Integer> octets, int octetPosition, int power) {
        return octets.get(octetPosition) * Math.pow(256, power);
    }

    public static String fromLongToString(Long longIp) {
        return convertLongToStringViaBitManipulation(longIp);
    }

    private static String convertLongToStringViaBitManipulation(Long longIp) {
        StringBuilder result = new StringBuilder();
        for (int bitShiftIndex = 0; bitShiftIndex < SHIFT_BITS_RIGHT.length; bitShiftIndex++) {
            String octetString = convertByteToOctetStringAtIndex(bitShiftIndex, longIp);
            appendOctetStringToResult(result, octetString);
            appendOctetDotToResultAtIndex(result, bitShiftIndex);
        }
        return result.toString();
    }

    private static String convertByteToOctetStringAtIndex(int bitShiftIndex, Long longIp) {
        return Long.toString((longIp >> SHIFT_BITS_RIGHT[bitShiftIndex]) & HEX_BITWISE_AND_MASK);
    }

    private static void appendOctetStringToResult(StringBuilder result, String octet) {
        result.append(octet);
    }

    private static void appendOctetDotToResultAtIndex(StringBuilder result, int bitShiftIndex) {
        if (notRightmostOctet(bitShiftIndex))
            result.append(".");
    }

    private static boolean notRightmostOctet(int bitShiftIndex) {
        return bitShiftIndex != SHIFT_BITS_RIGHT.length -1;
    }
}
