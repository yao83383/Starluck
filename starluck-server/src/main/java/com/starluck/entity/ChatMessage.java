package com.starluck.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 聊天消息表
 *
 * @author AI
 * @date 2026-06-01
 */
@Data
@TableName("chat_message")
public class ChatMessage {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 会话ID */
    private Long sessionId;

    /** 发送者ID */
    private Long senderId;

    /** 发送者角色：male男用户 female女用户 system系统 */
    private String senderRole;

    /** 消息类型：text文字 gift礼物 sys系统提示 image图片 */
    private String msgType;

    /** 消息内容 */
    private String content;

    /** 礼物emoji */
    private String giftEmoji;

    /** 礼物名称 */
    private String giftName;

    /** 消费钻石数 */
    private Integer costDiamond;

    /** 消息时间 */
    private String msgTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
