package com.ferrientregas.delivery.exception;

public class DeliveryBusinessLogicException extends RuntimeException {
    public DeliveryBusinessLogicException(String message) {
        super(
                "DeliveryBusinessLogicException: " + message
        );
    }
}
