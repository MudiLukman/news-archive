package com.kontrol.newsarchive.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtil {

    private static final Pattern pattern = Pattern.compile("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

    public static boolean isValid(String url) {
        if (url == null || url.trim().equals("")) {
            return false;
        }
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

}
