package com.starluck.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * 赠送礼物请求
 *
 * @author AI
 * @date 2026-06-01
 */
@Data
public class GiftSendRequest {
    @NotNull(message = "接收者ID不能为空")
    private Long receiverId;

    @NotNull(message = "礼物ID不能为空")
    private Long giftId;

    @Positive(message = "数量必须大于0")
    private Integer quantity;

    /** 关联会话ID（可选） */
    private Long sessionId;
}
