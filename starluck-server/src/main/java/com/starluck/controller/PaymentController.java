package com.starluck.controller;

import com.starluck.common.Result;
import com.starluck.common.SecurityUtil;
import com.starluck.dto.RechargeRequest;
import com.starluck.dto.WithdrawRequest;
import com.starluck.service.PaymentService;
import com.starluck.vo.WalletVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 支付控制器
 *
 * @author AI
 * @date 2026-06-01
 */
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * 获取钱包信息
     */
    @GetMapping("/wallet")
    public Result<WalletVO> getWallet() {
        Long userId = SecurityUtil.getCurrentUserId();
        return Result.ok(paymentService.getWallet(userId));
    }

    /**
     * 创建充值订单
     */
    @PostMapping("/recharge")
    public Result<String> recharge(@Valid @RequestBody RechargeRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        String orderNo = paymentService.createRechargeOrder(userId, request.getPackageId(), request.getPayMethod());
        return Result.ok("充值成功", orderNo);
    }

    /**
     * 申请提现
     */
    @PostMapping("/withdraw")
    public Result<Void> withdraw(@Valid @RequestBody WithdrawRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        paymentService.applyWithdraw(userId, request.getAmount(), request.getMethod(), request.getAccount());
        return Result.ok("提现申请已提交");
    }
}
