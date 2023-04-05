package com.example.myhome.util;

public enum UserRole {

    // директор, админ, сантехник, управляющий и т.д...
    ANY("Любой специалист"),
    DIRECTOR("Директор"),
    ADMIN("Администратор"),
    ACCOUNTANT("Бухгалтер"),
    PLUMBER("Сантехник"),
    MANAGER("Управляющий"),
    ELECTRICIAN("Электрик");

    private final String name;

    UserRole(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
