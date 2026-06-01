package com.starluck.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

@TableName("push_log")
public class PushLog {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long fakeUserId;
    private Long targetUserId;
    private String source;
    private String slot;
    private Boolean opened;
    private Boolean replied;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getFakeUserId() { return fakeUserId; }
    public void setFakeUserId(Long fakeUserId) { this.fakeUserId = fakeUserId; }
    public Long getTargetUserId() { return targetUserId; }
    public void setTargetUserId(Long targetUserId) { this.targetUserId = targetUserId; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getSlot() { return slot; }
    public void setSlot(String slot) { this.slot = slot; }
    public Boolean getOpened() { return opened; }
    public void setOpened(Boolean opened) { this.opened = opened; }
    public Boolean getReplied() { return replied; }
    public void setReplied(Boolean replied) { this.replied = replied; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
