package com.starluck.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 开发环境内存存储（替代Redis）
 *
 * @author AI
 * @date 2026-06-01
 */
@Configuration
@Profile("dev")
public class DevStoreConfig {

    /**
     * 验证码内存存储（key: sms:code:手机号, value: 验证码）
     */
    @Bean
    public Map<String, String> codeStore() {
        return new ConcurrentHashMap<>();
    }
}
