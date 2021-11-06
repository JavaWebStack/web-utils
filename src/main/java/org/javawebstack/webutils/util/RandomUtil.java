package org.javawebstack.webutils.util;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Random;

public class RandomUtil {

    public static final String LOWER_ALPHA = "abcdefghijklmnopqrstuvwxyz";
    public static final String UPPER_ALPHA = LOWER_ALPHA.toUpperCase(Locale.ROOT);
    public static final String ALPHA = LOWER_ALPHA + UPPER_ALPHA;
    public static final String NUMERIC = "1234567890";
    public static final String ALPHA_NUMERIC = ALPHA + NUMERIC;

    public static String string(int length) {
        return string(ALPHA_NUMERIC, length);
    }

    public static String string(String charset, int length) {
        Random random = new SecureRandom();
        char[] chars = new char[length];
        for(int i=0; i<length; i++)
            chars[i] = charset.charAt(random.nextInt(charset.length()));
        return new String(chars);
    }

}
