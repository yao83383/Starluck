package com.starluck.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("invite_record")
public class InviteRecord {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long inviterId;
    private Long inviteeId;
    private String status;
    private Integer rewardDiamond;
    private BigDecimal rewardCash;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getInviterId() { return inviterId; }
    public void setInviterId(Long inviterId) { this.inviterId = inviterId; }
    public Long getInviteeId() { return inviteeId; }
    public void setInviteeId(Long inviteeId) { this.inviteeId = inviteeId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getRewardDiamond() { return rewardDiamond; }
    public void setRewardDiamond(Integer rewardDiamond) { this.rewardDiamond = rewardDiamond; }
    public BigDecimal getRewardCash() { return rewardCash; }
    public void setRewardCash(BigDecimal rewardCash) { this.rewardCash = rewardCash; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
