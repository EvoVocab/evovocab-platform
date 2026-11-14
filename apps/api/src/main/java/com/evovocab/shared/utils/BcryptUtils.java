package com.evovocab.shared.utils;

import java.util.regex.Pattern;

public class BcryptUtils {

    private static final Pattern BCRYPT_PATTERN = Pattern.compile("\\$2b\\$10\\$[./A-Za-z0-9]{22}[./A-Za-z0-9]{31}");

    public static boolean checkIfPasswordIsHashed(String password) {
        return password != null && BCRYPT_PATTERN.matcher(password).matches();
    }
}
