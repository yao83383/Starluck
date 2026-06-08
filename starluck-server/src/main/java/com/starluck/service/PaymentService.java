package com.starluck.service;

import com.starluck.vo.WalletVO;
import java.math.BigDecimal;

/**
 * 支付服务接口
 *
 * @author AI
 * @date 2026-06-01
 */
public interface PaymentService {

    /**
     * 获取钱包信息
     */
    WalletVO getWallet(Long userId);

    /**
     * 创建充值订单
     */
    String createRechargeOrder(Long userId, Long packageId, String payMethod);

    /**
     * 支付回调处理
     */
    void handlePayCallback(String orderNo, String tradeNo);

    /**
     * 申请提现
     */
    void applyWithdraw(Long userId, BigDecimal amount, String method, String account);

    /**
     * 星尘兑换为可提现余额
     * @param userId 用户ID
     * @param amount 星尘数量
     * @return 兑换后现金余额
     */
    BigDecimal exchangePinkDiamonds(Long userId, Integer amount);
}
