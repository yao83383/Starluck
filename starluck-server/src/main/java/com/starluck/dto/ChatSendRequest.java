package com.starluck.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 发送消息请求
 *
 * @author AI
 * @date 2026-06-01
 */
@Data
public class ChatSendRequest {
    @NotNull(message = "会话ID不能为空")
    private Long sessionId;

    @NotBlank(message = "消息内容不能为空")
    private String content;
}
