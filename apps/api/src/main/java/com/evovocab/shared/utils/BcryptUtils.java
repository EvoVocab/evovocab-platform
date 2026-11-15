package com.evovocab.shared.utils;

import java.util.regex.Pattern;

public class BcryptUtils {

    private static final Pattern BCRYPT_PATTERN = Pattern.compile("^\\$2b\\$10\\$[./A-Za-z0-9]{53}$");

    public static boolean checkIfPasswordIsHashed(String password) {
        return BCRYPT_PATTERN.matcher(password).matches();
    }
}
