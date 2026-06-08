package com.starluck.vo;

import java.math.BigDecimal;

public class CsStatsVO {

    private Long userId;
    private String phone;
    private int fakeUserCount;
    private long totalPushCount;
    private long openedCount;
    private long repliedCount;
    private BigDecimal totalCashEarned;

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private CsStatsVO vo = new CsStatsVO();
        public Builder userId(Long v) { vo.userId = v; return this; }
        public Builder phone(String v) { vo.phone = v; return this; }
        public Builder fakeUserCount(int v) { vo.fakeUserCount = v; return this; }
        public Builder totalPushCount(long v) { vo.totalPushCount = v; return this; }
        public Builder openedCount(long v) { vo.openedCount = v; return this; }
        public Builder repliedCount(long v) { vo.repliedCount = v; return this; }
        public Builder totalCashEarned(BigDecimal v) { vo.totalCashEarned = v; return this; }
        public CsStatsVO build() { return vo; }
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public int getFakeUserCount() { return fakeUserCount; }
    public void setFakeUserCount(int fakeUserCount) { this.fakeUserCount = fakeUserCount; }
    public long getTotalPushCount() { return totalPushCount; }
    public void setTotalPushCount(long totalPushCount) { this.totalPushCount = totalPushCount; }
    public long getOpenedCount() { return openedCount; }
    public void setOpenedCount(long openedCount) { this.openedCount = openedCount; }
    public long getRepliedCount() { return repliedCount; }
    public void setRepliedCount(long repliedCount) { this.repliedCount = repliedCount; }
    public BigDecimal getTotalCashEarned() { return totalCashEarned; }
    public void setTotalCashEarned(BigDecimal totalCashEarned) { this.totalCashEarned = totalCashEarned; }
}
