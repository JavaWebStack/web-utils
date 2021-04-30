package org.javawebstack.webutils.crypt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Crypt {

    public static String generateKey() {
        return generateKey(256);
    }

    public static String generateKey(int length) {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(length);
            SecretKey secretKey = keyGen.generateKey();
            return new String(Base64.getEncoder().encode(secretKey.getEncoded()), StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException(e.getMessage());
        }
    }

    private final byte[] key;

    public Crypt(String key) {
        this.key = Base64.getDecoder().decode(key.getBytes(StandardCharsets.UTF_8));
    }

    public static String hash(String data) {
        return BCrypt.hash(data);
    }

    public static boolean check(String hash, String data) {
        return BCrypt.check(hash, data);
    }

    public String sign(String data) {
        return sign(data.getBytes(StandardCharsets.UTF_8));
    }

    public String sign(byte[] data) {
        Mac sha512Hmac;
        try {
            sha512Hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec keySpec = new SecretKeySpec(key, "HmacSHA512");
            sha512Hmac.init(keySpec);
            byte[] macData = sha512Hmac.doFinal(data);
            return new String(Base64.getEncoder().encode(macData), StandardCharsets.UTF_8);
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String encryptLaravel(String data) {
        return encrypt("s:" + data.length() + ":\"" + data + "\";");
    }

    public String encrypt(String data) {
        return encrypt(data.getBytes(StandardCharsets.UTF_8));
    }

    public String encrypt(byte[] data) {
        try {
            byte[] iv = new byte[16];
            new SecureRandom().nextBytes(iv);
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");

            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
            byte[] value = cipher.doFinal(data);
            byte[] mac = mac(Base64.getEncoder().encode(iv), Base64.getEncoder().encode(value));
            Map<String, String> cryptData = new HashMap<>();
            cryptData.put("iv", new String(Base64.getEncoder().encode(iv)));
            cryptData.put("value", new String(Base64.getEncoder().encode(value)));
            cryptData.put("mac", toHex(mac));
            return new String(Base64.getEncoder().encode(new GsonBuilder().disableHtmlEscaping().create().toJson(cryptData).getBytes(StandardCharsets.UTF_8)));
        } catch (IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException ex) {
            throw new SecurityException(ex.getMessage());
        }
    }

    private byte[] mac(byte[] base64iv, byte[] data) {
        try {
            byte[] full = new byte[base64iv.length + data.length];
            System.arraycopy(base64iv, 0, full, 0, base64iv.length);
            System.arraycopy(data, 0, full, base64iv.length, data.length);
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(key, "HmacSHA256");
            sha256Hmac.init(keySpec);
            return sha256Hmac.doFinal(full);
        } catch (InvalidKeyException | NoSuchAlgorithmException ex) {
            throw new SecurityException(ex.getMessage());
        }
    }

    public String decryptLaravel(String data) {
        String serialized = decryptString(data);
        String lenStr = serialized.split(":")[1];
        return serialized.substring(4 + lenStr.length(), 4 + lenStr.length() + Integer.parseInt(lenStr));
    }

    public String decryptString(String data) {
        return new String(decrypt(data), StandardCharsets.UTF_8);
    }

    public byte[] decrypt(String data) {
        try {
            JsonObject json = new Gson().fromJson(new String(Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8), JsonObject.class);
            if (!toHex(mac(json.get("iv").getAsString().getBytes(StandardCharsets.UTF_8), json.get("value").getAsString().getBytes(StandardCharsets.UTF_8))).equals(json.get("mac").getAsString()))
                throw new SecurityException("Invalid MAC");
            byte[] iv = Base64.getDecoder().decode(json.get("iv").getAsString().getBytes(StandardCharsets.UTF_8));
            byte[] value = Base64.getDecoder().decode(json.get("value").getAsString().getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            return cipher.doFinal(value);
        } catch (IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException ex) {
            throw new SecurityException(ex.getMessage());
        }
    }

    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String s = Integer.toString(b & 0xFF, 16);
            if (s.length() < 2)
                sb.append('0');
            sb.append(s);
        }
        return sb.toString();
    }

}
