package com.example.myhome.util;

public enum UserRole {

    // директор, админ, сантехник, управляющий и т.д...
    ROLE_ANY("Любой специалист"),
    ROLE_DIRECTOR("Директор"),
    ROLE_ADMIN("Администратор"),
    ROLE_ACCOUNTANT("Бухгалтер"),
    ROLE_PLUMBER("Сантехник"),
    ROLE_MANAGER("Управляющий"),
    ROLE_ELECTRICIAN("Электрик"),
    ROLE_USER("Пользователь");

    private final String name;

    UserRole(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
