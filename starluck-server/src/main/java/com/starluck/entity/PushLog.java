package com.starluck.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 推送日志表
 *
 * @author AI
 * @date 2026-06-01
 */
@Data
@TableName("push_log")
public class PushLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 假用户ID */
    private Long fakeUserId;

    /** 目标男用户ID */
    private Long targetUserId;

    /** 推送来源：manual手动 auto自动 */
    private String source;

    /** 对应时段编号 */
    private String slot;

    /** 是否已打开 */
    private Boolean opened;

    /** 是否已回复 */
    private Boolean replied;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
