package com.ferrientregas.role.utils;

import lombok.Getter;

@Getter
public enum RoleEnum {
    ADMIN("ADMIN"),
    CUSTOMER("CUSTOMER"),
    EMPLOYEE("EMPLOYEE"),
    DRIVER("DRIVER");

    private final String value;

    RoleEnum(String value) {
        this.value = value;
    }

    public static boolean isValid(String role) {
        for (RoleEnum roleEntity : values()) {
            if (roleEntity.getValue().equals(role)) {
                return true;
            }
        }
        return false;
    }
}
