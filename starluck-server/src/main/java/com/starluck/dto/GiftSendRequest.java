package com.starluck.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class GiftSendRequest {
    @NotNull(message = "接收者ID不能为空")
    private Long receiverId;

    @NotNull(message = "礼物ID不能为空")
    private Long giftId;

    @Positive(message = "数量必须大于0")
    private Integer quantity;

    private Long sessionId;

    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }
    public Long getGiftId() { return giftId; }
    public void setGiftId(Long giftId) { this.giftId = giftId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }
}
