package com.starluck;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 一青友人 · 社交交友平台后端服务
 *
 * @author AI
 * @date 2026-06-01
 * @version 1.0
 */
@SpringBootApplication
@MapperScan("com.starluck.mapper")
public class StarluckApplication {

    public static void main(String[] args) {
        SpringApplication.run(StarluckApplication.class, args);
    }
}
