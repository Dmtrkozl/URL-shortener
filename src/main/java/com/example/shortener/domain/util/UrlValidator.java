package com.example.shortener.domain.util;

import com.example.shortener.domain.Link;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlValidator {
    private static final Pattern URL_PATTERN = Pattern.compile("^https?://");

    public static boolean validateUrl(Link link) {
        Matcher matcher = URL_PATTERN.matcher(link.getUrl());
        return matcher.find();
    }
}
