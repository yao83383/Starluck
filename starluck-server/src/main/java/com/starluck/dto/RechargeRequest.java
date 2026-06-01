package com.starluck.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RechargeRequest {
    @NotNull(message = "套餐ID不能为空")
    private Long packageId;

    @NotBlank(message = "支付方式不能为空")
    private String payMethod;

    public Long getPackageId() { return packageId; }
    public void setPackageId(Long packageId) { this.packageId = packageId; }
    public String getPayMethod() { return payMethod; }
    public void setPayMethod(String payMethod) { this.payMethod = payMethod; }
}
