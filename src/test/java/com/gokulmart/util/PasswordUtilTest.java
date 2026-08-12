package com.gokulmart.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordUtilTest {

    @Test
    public void testHashPasswordAndCheckPassword() {
        String plainPassword = "buyerSecret123";
        String hash = PasswordUtil.hashPassword(plainPassword);

        assertNotNull(hash, "Password hash should not be null");
        assertNotEquals(plainPassword, hash, "Hash must not equal plain text password");
        assertTrue(PasswordUtil.checkPassword(plainPassword, hash), "Password verification should succeed for correct password");
        assertFalse(PasswordUtil.checkPassword("wrongPassword", hash), "Password verification should fail for incorrect password");
    }

    @Test
    public void testEmptyPasswordThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            PasswordUtil.hashPassword("");
        });
    }
}
