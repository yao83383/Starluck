package com.starluck.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 钱包响应
 *
 * @author AI
 * @date 2026-06-01
 */
@Data
@Builder
public class WalletVO {

    private Integer diamonds;
    private BigDecimal cash;

    /** 本月收益明细 */
    private BigDecimal monthGiftIncome;
    private BigDecimal monthChatIncome;
    private BigDecimal monthCallIncome;

    /** 最近交易记录 */
    private List<TransactionVO> recentTransactions;
}
