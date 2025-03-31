package com.ferrientregas.delivery.service;

import com.ferrientregas.delivery.utils.DeliveryStatusEnum;
import com.ferrientregas.delivery.utils.PaymentTypeEnum;
import com.ferrientregas.deliverystatus.DeliveryStatusEntity;
import com.ferrientregas.deliverystatus.DeliveryStatusRepository;
import com.ferrientregas.paymenttype.PaymentTypeEntity;
import com.ferrientregas.paymenttype.PaymentTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateEntitiesDefaultService {

    private final DeliveryStatusRepository deliveryStatusRepository;
    private final PaymentTypeRepository paymentTypeRepository;

    public DeliveryStatusEntity getOrCreateDeliveryStatus(String deliveryStatus) {
        return this.deliveryStatusRepository.findByName(deliveryStatus)
                .orElseGet(() -> {
                    if (DeliveryStatusEnum.isValid(deliveryStatus)) {
                        return deliveryStatusRepository.save(new DeliveryStatusEntity(deliveryStatus));
                    } else {
                        throw new EntityNotFoundException("Delivery status not found");
                    }
                });
    }

    public PaymentTypeEntity getOrCreatePaymentType(String paymentType) {
        return this.paymentTypeRepository.findByName(paymentType)
                .orElseGet(() -> {
                    if (PaymentTypeEnum.isValid(paymentType)) {
                        return paymentTypeRepository.save(new PaymentTypeEntity(paymentType));
                    } else {
                        throw new EntityNotFoundException("Payment type not found");
                    }
                });
    }
}
