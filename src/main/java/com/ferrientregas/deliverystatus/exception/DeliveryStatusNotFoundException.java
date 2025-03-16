package com.ferrientregas.deliverystatus.exception;

public class DeliveryStatusNotFoundException extends Exception {
    public DeliveryStatusNotFoundException() {
        super("Estado de entrega no encontrado.");
    }
}
