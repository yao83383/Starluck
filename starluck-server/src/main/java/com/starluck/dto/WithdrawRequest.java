package com.starluck.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 提现请求
 *
 * @author AI
 * @date 2026-06-01
 */
@Data
public class WithdrawRequest {
    @NotNull(message = "提现金额不能为空")
    @Positive(message = "提现金额必须大于0")
    private BigDecimal amount;

    @NotBlank(message = "提现方式不能为空")
    private String method;

    /** 收款账号 */
    private String account;
}
