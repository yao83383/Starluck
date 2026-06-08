package com.starluck.vo;

import java.math.BigDecimal;
import java.util.List;

public class WalletVO {

    private Integer diamonds;
    private Integer pinkDiamonds;
    private BigDecimal cash;
    private BigDecimal monthGiftIncome;
    private BigDecimal monthChatIncome;
    private BigDecimal monthCallIncome;
    private List<TransactionVO> recentTransactions;

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private WalletVO vo = new WalletVO();
        public Builder diamonds(Integer v) { vo.diamonds = v; return this; }
        public Builder pinkDiamonds(Integer v) { vo.pinkDiamonds = v; return this; }
        public Builder cash(BigDecimal v) { vo.cash = v; return this; }
        public Builder monthGiftIncome(BigDecimal v) { vo.monthGiftIncome = v; return this; }
        public Builder monthChatIncome(BigDecimal v) { vo.monthChatIncome = v; return this; }
        public Builder monthCallIncome(BigDecimal v) { vo.monthCallIncome = v; return this; }
        public Builder recentTransactions(List<TransactionVO> v) { vo.recentTransactions = v; return this; }
        public WalletVO build() { return vo; }
    }

    public Integer getDiamonds() { return diamonds; }
    public void setDiamonds(Integer diamonds) { this.diamonds = diamonds; }
    public Integer getPinkDiamonds() { return pinkDiamonds; }
    public void setPinkDiamonds(Integer pinkDiamonds) { this.pinkDiamonds = pinkDiamonds; }
    public BigDecimal getCash() { return cash; }
    public void setCash(BigDecimal cash) { this.cash = cash; }
    public BigDecimal getMonthGiftIncome() { return monthGiftIncome; }
    public void setMonthGiftIncome(BigDecimal monthGiftIncome) { this.monthGiftIncome = monthGiftIncome; }
    public BigDecimal getMonthChatIncome() { return monthChatIncome; }
    public void setMonthChatIncome(BigDecimal monthChatIncome) { this.monthChatIncome = monthChatIncome; }
    public BigDecimal getMonthCallIncome() { return monthCallIncome; }
    public void setMonthCallIncome(BigDecimal monthCallIncome) { this.monthCallIncome = monthCallIncome; }
    public List<TransactionVO> getRecentTransactions() { return recentTransactions; }
    public void setRecentTransactions(List<TransactionVO> recentTransactions) { this.recentTransactions = recentTransactions; }
}
