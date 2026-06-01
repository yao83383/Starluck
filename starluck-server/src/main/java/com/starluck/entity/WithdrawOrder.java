package com.starluck.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 提现订单表
 *
 * @author AI
 * @date 2026-06-01
 */
@Data
@TableName("withdraw_order")
public class WithdrawOrder {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 订单号 */
    private String orderNo;

    /** 用户ID */
    private Long userId;

    /** 提现金额（元） */
    private BigDecimal amount;

    /** 手续费（元） */
    private BigDecimal fee;

    /** 实际到账（元） */
    private BigDecimal actualAmount;

    /** 提现方式：wechat微信 alipay支付宝 bank银行卡 */
    private String method;

    /** 收款账号 */
    private String account;

    /** 状态：pending待审核 approved已通过 rejected已拒绝 paid已打款 */
    private String status;

    /** 审核备注 */
    private String auditRemark;

    /** 打款时间 */
    private LocalDateTime paidAt;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
