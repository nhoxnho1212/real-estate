package com.ou.ret.util.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class AES256EncryptionUtilImplTest {
    private final String SECRET_KEY = "abc";
    private final String SALT = "testSail";
    private AES256EncryptionUtilImpl aes256EncryptionUtil;

    @BeforeEach
    public void setUp() {
        aes256EncryptionUtil = new AES256EncryptionUtilImpl(SECRET_KEY, SALT);
    }

    @Test
    void testEncryption() {
        String payload = "test encryption algorithm";

        String encryptPayload = aes256EncryptionUtil.encrypt(payload);
        String decryptPayload = aes256EncryptionUtil.decrypt(encryptPayload);

        assertEquals(payload, decryptPayload);
        assertEquals(44, encryptPayload.length());
    }
}
