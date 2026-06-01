package com.starluck.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 充值订单表
 *
 * @author AI
 * @date 2026-06-01
 */
@Data
@TableName("recharge_order")
public class RechargeOrder {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 订单号 */
    private String orderNo;

    /** 用户ID */
    private Long userId;

    /** 套餐ID */
    private Long packageId;

    /** 充值金额（元） */
    private BigDecimal amount;

    /** 获得钻石数 */
    private Integer diamonds;

    /** 赠送钻石数 */
    private Integer bonusDiamonds;

    /** 支付方式：wechat微信 alipay支付宝 apple苹果 */
    private String payMethod;

    /** 支付状态：pending待支付 paid已支付 cancelled已取消 refunded已退款 */
    private String status;

    /** 第三方支付流水号 */
    private String tradeNo;

    /** 支付时间 */
    private LocalDateTime paidAt;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
