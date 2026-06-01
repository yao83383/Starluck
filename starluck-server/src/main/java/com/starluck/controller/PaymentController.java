package com.starluck.controller;

import com.starluck.common.Result;
import com.starluck.common.SecurityUtil;
import com.starluck.dto.RechargeRequest;
import com.starluck.dto.WithdrawRequest;
import com.starluck.service.PaymentService;
import com.starluck.vo.WalletVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/wallet")
    public Result<WalletVO> getWallet() {
        Long userId = SecurityUtil.getCurrentUserId();
        return Result.ok(paymentService.getWallet(userId));
    }

    @PostMapping("/recharge")
    public Result<String> recharge(@Valid @RequestBody RechargeRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        String orderNo = paymentService.createRechargeOrder(userId, request.getPackageId(), request.getPayMethod());
        return Result.ok("充值成功", orderNo);
    }

    @PostMapping("/withdraw")
    public Result<Void> withdraw(@Valid @RequestBody WithdrawRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        paymentService.applyWithdraw(userId, request.getAmount(), request.getMethod(), request.getAccount());
        return Result.okMsg("提现申请已提交");
    }
}
