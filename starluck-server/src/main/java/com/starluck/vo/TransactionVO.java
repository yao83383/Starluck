package com.starluck.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 交易记录响应
 *
 * @author AI
 * @date 2026-06-01
 */
@Data
@Builder
public class TransactionVO {

    private Long id;
    private String type;
    private BigDecimal amount;
    private String bizType;
    private String description;
    private String createdAt;
}
