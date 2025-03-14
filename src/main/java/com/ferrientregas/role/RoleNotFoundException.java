package com.ferrientregas.role;

public class RoleNotFoundException extends Exception {
    public RoleNotFoundException() {
        super("Rol no encontrado.");
    }
}
