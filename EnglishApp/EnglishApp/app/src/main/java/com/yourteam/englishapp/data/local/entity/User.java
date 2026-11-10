package com.yourteam.englishapp.data.local.entity;

import com.yourteam.englishapp.model.Role;

/** POJO ánh xạ bảng Users (đóng gói: field private + getter/setter). */
public class User {
    private long id;
    private String fullName;
    private String email;
    private String passwordHash;   // KHÔNG lưu mật khẩu thô
    private String salt;
    private Role role = Role.USER;
    private boolean active = true; // Admin có thể khóa tài khoản
    private long createdAt;

    public User() {}

    public User(String fullName, String email, String passwordHash, String salt, Role role) {
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.role = role;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getSalt() { return salt; }
    public void setSalt(String salt) { this.salt = salt; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
