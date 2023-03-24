package com.example.myhome.util;

public enum UserRole {

    // директор, админ, сантехник, управляющий и т.д...
    DIRECTOR("Директор"),
    ADMIN("Администратор"),
    TECHNICIAN("Сантехник"),
    MANAGER("Управляющий");

    private final String name;

    UserRole(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
