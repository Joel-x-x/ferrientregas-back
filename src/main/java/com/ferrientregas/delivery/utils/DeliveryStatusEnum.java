package com.ferrientregas.delivery.utils;

public enum DeliveryStatusEnum {
    Pendiente("Pendiente"),
    EnProceso("En-Proceso"),
    Incompleto("Incompleto"),
    Entregado("Entregado"),
    Atrasado("Atrasado"),
    Cancelado("Cancelado");

    private final String value;

    DeliveryStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
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
