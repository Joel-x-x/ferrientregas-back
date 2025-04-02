package com.ferrientregas.paymenttype.utils;

import lombok.Getter;

@Getter
public enum PaymentTypeEnum {
    CREDITO("Credito"),
    CONTRAENTREGA("Contra-Entrega"),
    INCOMPLETO("Incompleto"),
    PAGADO("Pagado"),
    ABONADO("Abonado");

    private final String value;

    PaymentTypeEnum(String value) {
        this.value = value;
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
