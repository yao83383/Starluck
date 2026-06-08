package com.starluck.vo;

public class AdminUserVO {

    private Long id;
    private String phone;
    private String role;
    private Integer status;
    private Integer fakeUserCount;
    private String lastLoginTime;

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private AdminUserVO vo = new AdminUserVO();
        public Builder id(Long v) { vo.id = v; return this; }
        public Builder phone(String v) { vo.phone = v; return this; }
        public Builder role(String v) { vo.role = v; return this; }
        public Builder status(Integer v) { vo.status = v; return this; }
        public Builder fakeUserCount(Integer v) { vo.fakeUserCount = v; return this; }
        public Builder lastLoginTime(String v) { vo.lastLoginTime = v; return this; }
        public AdminUserVO build() { return vo; }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getFakeUserCount() { return fakeUserCount; }
    public void setFakeUserCount(Integer fakeUserCount) { this.fakeUserCount = fakeUserCount; }
    public String getLastLoginTime() { return lastLoginTime; }
    public void setLastLoginTime(String lastLoginTime) { this.lastLoginTime = lastLoginTime; }
}
