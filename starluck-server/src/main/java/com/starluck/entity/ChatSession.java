package com.starluck.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

/**
 * 聊天会话表
 *
 * @author AI
 * @date 2026-06-01
 */
@TableName("chat_session")
public class ChatSession {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long maleUserId;
    private Long femaleUserId;
    private Boolean isFake;
    private String lastMsg;
    private String lastTime;
    private Integer maleUnread;
    private Integer femaleUnread;
    private String type;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMaleUserId() { return maleUserId; }
    public void setMaleUserId(Long maleUserId) { this.maleUserId = maleUserId; }
    public Long getFemaleUserId() { return femaleUserId; }
    public void setFemaleUserId(Long femaleUserId) { this.femaleUserId = femaleUserId; }
    public Boolean getIsFake() { return isFake; }
    public void setIsFake(Boolean isFake) { this.isFake = isFake; }
    public String getLastMsg() { return lastMsg; }
    public void setLastMsg(String lastMsg) { this.lastMsg = lastMsg; }
    public String getLastTime() { return lastTime; }
    public void setLastTime(String lastTime) { this.lastTime = lastTime; }
    public Integer getMaleUnread() { return maleUnread; }
    public void setMaleUnread(Integer maleUnread) { this.maleUnread = maleUnread; }
    public Integer getFemaleUnread() { return femaleUnread; }
    public void setFemaleUnread(Integer femaleUnread) { this.femaleUnread = femaleUnread; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
