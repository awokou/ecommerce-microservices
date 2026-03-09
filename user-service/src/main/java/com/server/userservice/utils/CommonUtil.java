package com.server.userservice.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ThreadLocalRandom;

public class CommonUtil {

    public static final String NUMBER_DIGITS = "0123456789";

    public static String getRandomNum() {
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            int num = ThreadLocalRandom.current().nextInt(NUMBER_DIGITS.length());
            sb.append(NUMBER_DIGITS.charAt(num));
        }
        return sb.toString();
    }

    public static String sha256Hash(String message) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hash = digest.digest(message.getBytes(StandardCharsets.UTF_8));

        // Convert byte array to hex string
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = String.format("%02x", b);
            hexString.append(hex);
        }

        return hexString.toString();
    }
}
