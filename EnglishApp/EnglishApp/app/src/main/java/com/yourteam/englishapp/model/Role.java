package com.yourteam.englishapp.model;

/** Vai trò người dùng. Enum có hành vi (đa hình theo quyền). */
public enum Role {
    USER, ADMIN;

    public boolean canManageUsers() {
        return this == ADMIN;
    }
}
