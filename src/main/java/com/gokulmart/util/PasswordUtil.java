package com.gokulmart.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    /**
     * Hashes a plain-text password using jBCrypt.
     */
    public static String hashPassword(String plainTextPassword) {
        if (plainTextPassword == null || plainTextPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(10));
    }

    /**
     * Verifies a plain-text password against a stored BCrypt hash.
     */
    public static boolean checkPassword(String plainTextPassword, String storedHash) {
        if (plainTextPassword == null || storedHash == null || storedHash.isEmpty()) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainTextPassword, storedHash);
        } catch (Exception e) {
            return false;
        }
    }
}
