package com.starluck.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易流水表
 *
 * @author AI
 * @date 2026-06-01
 */
@Data
@TableName("transaction")
public class Transaction {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 类型：income收入 expense支出 */
    private String type;

    /** 金额（元） */
    private BigDecimal amount;

    /** 业务类型 */
    private String bizType;

    /** 关联业务ID */
    private Long refId;

    /** 交易描述 */
    private String description;

    /** 交易后余额 */
    private BigDecimal balanceAfter;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
