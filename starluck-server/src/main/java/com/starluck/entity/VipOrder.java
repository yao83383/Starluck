package com.starluck.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * VIP订单表
 *
 * @author AI
 * @date 2026-06-01
 */
@Data
@TableName("vip_order")
public class VipOrder {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 订单号 */
    private String orderNo;

    /** 用户ID */
    private Long userId;

    /** 套餐类型：month月卡 quarter季卡 year年卡 */
    private String planType;

    /** 支付金额（元） */
    private BigDecimal amount;

    /** 支付方式 */
    private String payMethod;

    /** 支付状态：pending待支付 paid已支付 cancelled已取消 */
    private String status;

    /** 第三方支付流水号 */
    private String tradeNo;

    /** VIP开始时间 */
    private LocalDateTime startTime;

    /** VIP到期时间 */
    private LocalDateTime expireTime;

    /** 支付时间 */
    private LocalDateTime paidAt;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
