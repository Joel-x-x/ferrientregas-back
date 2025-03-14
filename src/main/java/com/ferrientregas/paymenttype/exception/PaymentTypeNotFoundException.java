package com.ferrientregas.paymenttype.exception;

public class PaymentTypeNotFoundException extends Exception{
    public PaymentTypeNotFoundException() {
        super("Método de pago no encontrado");
    }
}
