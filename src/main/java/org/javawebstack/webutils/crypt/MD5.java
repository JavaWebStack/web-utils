package org.javawebstack.webutils.crypt;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

    public static String hashToString(byte[] data) {
        return Crypt.toHex(hash(data));
    }

    public static String hashToString(String data) {
        return Crypt.toHex(hash(data));
    }

    public static byte[] hash(String data) {
        return hash(data.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] hash(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

}
