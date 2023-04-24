package com.example.myhome.util;

public enum UserStatus {
    ACTIVE("Активен"),
    NEW("Новый"),
    DISABLED("Отключен"),
    INACTIVE("Неактивен");

    private final String name;

    UserStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
