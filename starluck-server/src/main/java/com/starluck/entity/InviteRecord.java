package com.starluck.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 邀请记录表
 *
 * @author AI
 * @date 2026-06-01
 */
@Data
@TableName("invite_record")
public class InviteRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 邀请人ID */
    private Long inviterId;

    /** 被邀请人ID */
    private Long inviteeId;

    /** 被邀请人状态：registered已注册 authed已实名 recharged已充值 */
    private String status;

    /** 奖励钻石数 */
    private Integer rewardDiamond;

    /** 现金返佣（元） */
    private BigDecimal rewardCash;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
