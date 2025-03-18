package com.ferrientregas.paymenttype.dto;

import com.ferrientregas.paymenttype.PaymentTypeEntity;

public class PaymentTypeMapper {
    public static PaymentTypeResponse toPaymentTypeResponse(
            PaymentTypeEntity paymentType){
        return new PaymentTypeResponse(
                paymentType.getId(),
                paymentType.getName()
        );
    }
}
