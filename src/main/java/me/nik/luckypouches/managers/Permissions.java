package me.nik.luckypouches.managers;

public enum Permissions {
    ADMIN("luckypouches.admin");

    private final String permission;

    Permissions(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return this.permission;
    }
}