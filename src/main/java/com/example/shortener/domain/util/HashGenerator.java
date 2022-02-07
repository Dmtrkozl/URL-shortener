package com.example.shortener.domain.util;

import java.util.Random;
import java.util.stream.Collectors;

public class HashGenerator {
    public static String getRandomHash() {
        int HASH_LENGTH = 6;
        String symbols = "abcdefghijklmnopqrstuvwxyz0123456789";
        return new Random().ints(HASH_LENGTH, 0, symbols.length())
                .mapToObj(symbols::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}