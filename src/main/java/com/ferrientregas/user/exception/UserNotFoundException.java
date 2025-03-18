package com.ferrientregas.user.exception;

public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super("Usuario no encontrado");
    }
}
