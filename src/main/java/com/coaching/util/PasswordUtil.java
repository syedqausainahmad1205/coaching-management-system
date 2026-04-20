package com.coaching.util;

import com.coaching.exception.ValidationException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordUtil {
    private static final int SALT_BYTES = 16;
    private static final int ITERATIONS = 65_536;
    private static final int KEY_BITS = 256;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private PasswordUtil() {
    }

    public static String hash(String password) {
        ValidationUtil.validatePassword(password);
        byte[] salt = new byte[SALT_BYTES];
        SECURE_RANDOM.nextBytes(salt);
        byte[] hashed = pbkdf2(password, salt, ITERATIONS, KEY_BITS);
        return ITERATIONS + ":" + Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hashed);
    }

    public static boolean verify(String password, String passwordHash) {
        if (passwordHash == null || passwordHash.isBlank()) {
            return false;
        }
        try {
            String[] parts = passwordHash.split(":");
            if (parts.length == 3) {
                int iterations = Integer.parseInt(parts[0]);
                byte[] salt = Base64.getDecoder().decode(parts[1]);
                byte[] expected = Base64.getDecoder().decode(parts[2]);
                byte[] actual = pbkdf2(password, salt, iterations, expected.length * 8);
                return MessageDigest.isEqual(expected, actual);
            }
            return verifyLegacySha256(password, passwordHash);
        } catch (RuntimeException e) {
            throw new ValidationException("Unable to process password");
        }
    }

    private static byte[] pbkdf2(String password, byte[] salt, int iterations, int bits) {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, bits);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new ValidationException("Unable to process password");
        } finally {
            spec.clearPassword();
        }
    }

    private static boolean verifyLegacySha256(String password, String passwordHash) {
        if (!passwordHash.matches("^[a-f0-9]{64}$")) {
            return false;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder(hashed.length * 2);
            for (byte b : hashed) {
                builder.append(String.format("%02x", b));
            }
            return MessageDigest.isEqual(builder.toString().getBytes(StandardCharsets.UTF_8),
                    passwordHash.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new ValidationException("Unable to process password");
        }
    }
}
