package com.ferrientregas.delivery.utils;

public enum PaymentTypeEnum {
    Credito("Credito"),
    ContraEntrega("Contra-Entrega"),
    Incompleto("Incompleto"),
    Pagado("Pagado"),
    Abonado("Abonado");

    private final String value;

    PaymentTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean isValid(String paymentType) {
        for (PaymentTypeEnum payment : values()) {
            if (payment.getValue().equals(paymentType)) {
                return true;
            }
        }
        return false;
    }
}
