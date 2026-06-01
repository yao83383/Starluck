package com.starluck.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 推送规则配置表
 *
 * @author AI
 * @date 2026-06-01
 */
@Data
@TableName("push_rule")
public class PushRule {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 是否启用自动推送 */
    private Boolean enabled;

    /** 时间段配置（JSON数组：[{start,end,maxPer}]） */
    private String timeSlots;

    /** 选人策略：random随机 active活跃优先 vip优先 */
    private String strategy;

    /** 同一假用户冷却时间（分钟） */
    private Integer cooldownMin;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
