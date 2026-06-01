package com.starluck.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 赠送礼物记录表
 *
 * @author AI
 * @date 2026-06-01
 */
@Data
@TableName("gift_record")
public class GiftRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 赠送者ID */
    private Long senderId;

    /** 接收者ID */
    private Long receiverId;

    /** 礼物ID */
    private Long giftId;

    /** 礼物名称 */
    private String giftName;

    /** 礼物图标 */
    private String giftIcon;

    /** 数量 */
    private Integer quantity;

    /** 单价（钻石） */
    private Integer unitPrice;

    /** 总价（钻石） */
    private Integer totalDiamond;

    /** 关联会话ID */
    private Long sessionId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
