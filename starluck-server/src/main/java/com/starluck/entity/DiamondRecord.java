package com.starluck.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 钻石流水表
 *
 * @author AI
 * @date 2026-06-01
 */
@Data
@TableName("diamond_record")
public class DiamondRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 类型：in收入 out支出 */
    private String type;

    /** 钻石数量变化（正为收入，负为支出） */
    private Integer amount;

    /** 变动后余额 */
    private Integer balanceAfter;

    /** 业务类型：recharge充值 chat聊天 gift送礼 withdraw提现 invite邀请 vip赠送 */
    private String bizType;

    /** 关联业务ID */
    private Long refId;

    /** 备注 */
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
