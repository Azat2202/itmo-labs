package com.example.is_coursework.utils;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class RandomHelper {
    private static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static Set<Long> getRandomList(Random random, int size, int min, int max) {
        return random.longs(size, min, max).boxed().distinct().collect(Collectors.toSet());
    }

    public static String getRandomStringUppercase(Random random, int length) {
        return random.ints(length, 0, alphabet.length())
                .mapToObj(alphabet::charAt)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
}
