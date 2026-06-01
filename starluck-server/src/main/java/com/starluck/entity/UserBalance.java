package com.starluck.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户余额表
 *
 * @author AI
 * @date 2026-06-01
 */
@Data
@TableName("user_balance")
public class UserBalance {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Integer diamonds;

    private BigDecimal cash;

    private BigDecimal totalRecharged;

    private BigDecimal totalWithdrawn;

    private Integer isVip;

    private LocalDateTime vipExpireTime;

    private Integer dailyFreeChat;

    private Integer isAuthed;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
