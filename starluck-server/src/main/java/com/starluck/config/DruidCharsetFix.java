package com.starluck.config;

import com.alibaba.druid.filter.FilterAdapter;
import com.alibaba.druid.filter.FilterChain;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.proxy.jdbc.ConnectionProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * 🐛 修复 Druid 中文乱码：强制每条新连接执行 SET NAMES utf8mb4
 *
 * 问题：MySQL JDBC 驱动在建立连接后，MySQL 服务端将 client charset 设为 latin1，
 * 导致所有中文写入被静默转换为 ??。即使 URL 带了 characterEncoding=UTF-8 也不生效。
 *
 * @author AI
 * @date 2026-06-04
 */
@Configuration
public class DruidCharsetFix implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(DruidCharsetFix.class);

    @Autowired(required = false)
    private DruidDataSource dataSource;

    @Override
    public void afterPropertiesSet() {
        if (dataSource == null) {
            log.warn("DruidDataSource not found, charset fix skipped");
            return;
        }
        dataSource.getProxyFilters().add(new FilterAdapter() {
            @Override
            public ConnectionProxy connection_connect(FilterChain chain, Properties info) throws SQLException {
                ConnectionProxy conn = chain.connection_connect(info);
                // 强制设置会话字符集为 utf8mb4
                try (Statement stmt = conn.getRawObject().createStatement()) {
                    stmt.execute("SET NAMES utf8mb4");
                    log.info("SET NAMES utf8mb4 — connection {} initialized", conn.getRawObject().hashCode());
                }
                return conn;
            }
        });
        log.info("Druid charset fix loaded — SET NAMES utf8mb4 on every new connection");
    }
}
