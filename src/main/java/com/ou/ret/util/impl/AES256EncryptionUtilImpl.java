package com.ou.ret.util.impl;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

import com.ou.ret.util.EncryptionUtil;

/**
 * AES (Advanced Encryption Standard) is a symmetric encryption algorithm.
 * AES 256-bit key can be expressed as hexadecimal string with 64 characters
 * and require 44 characters in base 64.
 */
public class AES256EncryptionUtilImpl implements EncryptionUtil {
    private final Logger LOGGER = LoggerFactory.getLogger(AES256EncryptionUtilImpl.class);
    private final String SECRET_KEY;
    private final String SALT;

    public AES256EncryptionUtilImpl(String SECRET_KEY, String salt) {
        this.SECRET_KEY = SECRET_KEY;
        this.SALT = salt;
    }

    @Override
    @Cacheable(value = "encrypt", key = "#payload")
    public String encrypt(String payload) {
        try {
            long startTimestamp = System.currentTimeMillis();
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Completed decrypting payload [{}] - elapsed time [{}]ms", payload, System.currentTimeMillis() - startTimestamp);
            }

            return Base64.getEncoder()
                         .encodeToString(cipher.doFinal(payload.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            LOGGER.error("Failed to encrypt payload [{}] - exception {}", payload, e.getMessage(), e);
        }
        return null;
    }

    @Override
    @Cacheable(value = "decrypt", key = "#payload")
    public String decrypt(String payload) {
        try {
            long startTimestamp = System.currentTimeMillis();

            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Completed decrypting payload [{}] - elapsed time [{}]ms", payload, System.currentTimeMillis() - startTimestamp);
            }
            return new String(cipher.doFinal(Base64.getDecoder().decode(payload)));
        } catch (Exception e) {
            LOGGER.error("Failed to decrypt payload [{}] - exception {}", payload, e.getMessage(), e);
        }
        return null;
    }

    public String getSecretKey() {
        return SECRET_KEY;
    }

    public String getSalt() {
        return SALT;
    }

}
