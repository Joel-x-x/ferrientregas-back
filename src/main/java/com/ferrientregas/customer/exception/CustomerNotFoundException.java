package com.ferrientregas.customer.exception;

public class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException() {
        super("Cliente no encontrado");
    }
}
