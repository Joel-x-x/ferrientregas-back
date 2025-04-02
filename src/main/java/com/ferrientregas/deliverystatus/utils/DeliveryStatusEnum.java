package com.ferrientregas.deliverystatus.utils;

import lombok.Getter;

@Getter
public enum DeliveryStatusEnum {
    PENDIENTE("Pendiente"),
    ENPROCESO("En-Proceso"),
    INCOMPLETO("Incompleto"),
    ENTREGADO("Entregado"),
    ATRASADO("Atrasado"),
    CANCELADO("Cancelado");

    private final String value;

    DeliveryStatusEnum(String value) {
        this.value = value;
    }

    public static boolean isValid(String status) {
        for (DeliveryStatusEnum deliveryStatus : values()) {
            if (deliveryStatus.getValue().equals(status)) {
                return true;
            }
        }
        return false;
    }
}
