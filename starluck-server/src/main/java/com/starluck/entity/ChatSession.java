package com.starluck.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 聊天会话表
 *
 * @author AI
 * @date 2026-06-01
 */
@Data
@TableName("chat_session")
public class ChatSession {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 男用户ID */
    private Long maleUserId;

    /** 女用户ID（真用户或假用户） */
    private Long femaleUserId;

    /** 对方是假用户：true */
    private Boolean isFake;

    /** 最后一条消息内容 */
    private String lastMsg;

    /** 最后消息时间 */
    private String lastTime;

    /** 男用户未读数 */
    private Integer maleUnread;

    /** 女用户未读数 */
    private Integer femaleUnread;

    /** 会话类型：chat聊天 gift礼物 */
    private String type;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
