package com.starluck.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 充值请求
 *
 * @author AI
 * @date 2026-06-01
 */
@Data
public class RechargeRequest {
    @NotNull(message = "套餐ID不能为空")
    private Long packageId;

    @NotBlank(message = "支付方式不能为空")
    private String payMethod;
}
