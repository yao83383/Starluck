package com.starluck.vo;

import java.math.BigDecimal;

public class TransactionVO {

    private Long id;
    private String type;
    private BigDecimal amount;
    private String bizType;
    private String description;
    private String createdAt;

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private TransactionVO vo = new TransactionVO();
        public Builder id(Long v) { vo.id = v; return this; }
        public Builder type(String v) { vo.type = v; return this; }
        public Builder amount(BigDecimal v) { vo.amount = v; return this; }
        public Builder bizType(String v) { vo.bizType = v; return this; }
        public Builder description(String v) { vo.description = v; return this; }
        public Builder createdAt(String v) { vo.createdAt = v; return this; }
        public TransactionVO build() { return vo; }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getBizType() { return bizType; }
    public void setBizType(String bizType) { this.bizType = bizType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
