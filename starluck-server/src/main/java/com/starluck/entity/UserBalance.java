package com.starluck.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户余额表
 *
 * @author AI
 * @date 2026-06-01
 */
@TableName("user_balance")
public class UserBalance {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Integer diamonds;
    private BigDecimal cash;
    private BigDecimal totalRecharged;
    private BigDecimal totalWithdrawn;
    private Integer isVip;
    private LocalDateTime vipExpireTime;
    private Integer dailyFreeChat;
    private Integer isAuthed;
    private Integer pinkDiamonds;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Integer getDiamonds() { return diamonds; }
    public void setDiamonds(Integer diamonds) { this.diamonds = diamonds; }
    public BigDecimal getCash() { return cash; }
    public void setCash(BigDecimal cash) { this.cash = cash; }
    public BigDecimal getTotalRecharged() { return totalRecharged; }
    public void setTotalRecharged(BigDecimal totalRecharged) { this.totalRecharged = totalRecharged; }
    public BigDecimal getTotalWithdrawn() { return totalWithdrawn; }
    public void setTotalWithdrawn(BigDecimal totalWithdrawn) { this.totalWithdrawn = totalWithdrawn; }
    public Integer getIsVip() { return isVip; }
    public void setIsVip(Integer isVip) { this.isVip = isVip; }
    public LocalDateTime getVipExpireTime() { return vipExpireTime; }
    public void setVipExpireTime(LocalDateTime vipExpireTime) { this.vipExpireTime = vipExpireTime; }
    public Integer getDailyFreeChat() { return dailyFreeChat; }
    public void setDailyFreeChat(Integer dailyFreeChat) { this.dailyFreeChat = dailyFreeChat; }
    public Integer getIsAuthed() { return isAuthed; }
    public void setIsAuthed(Integer isAuthed) { this.isAuthed = isAuthed; }
    public Integer getPinkDiamonds() { return pinkDiamonds; }
    public void setPinkDiamonds(Integer pinkDiamonds) { this.pinkDiamonds = pinkDiamonds; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
