package com.ou.ret.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ou.ret.util.EncryptionUtil;
import com.ou.ret.util.impl.AES256EncryptionUtilImpl;

@Configuration
public class EncryptionUtilConfig {
    private final EncryptionConfig config;

    @Autowired
    public EncryptionUtilConfig(EncryptionConfig config) {
        this.config = config;
    }

    @Bean
    public EncryptionUtil getAes() {
        return new AES256EncryptionUtilImpl(config.getAes().getSecretKey(), config.getAes().getSalt());
    }

}
