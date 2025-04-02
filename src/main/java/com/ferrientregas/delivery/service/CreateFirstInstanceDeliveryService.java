package com.ferrientregas.delivery.service;

import com.ferrientregas.delivery.DeliveryEntity;
import com.ferrientregas.delivery.DeliveryRepository;
import com.ferrientregas.delivery.dto.DeliveryResponse;
import com.ferrientregas.delivery.utils.DeliveryMapper;
import com.ferrientregas.deliverystatus.utils.DeliveryStatusEnum;
import com.ferrientregas.paymenttype.utils.PaymentTypeEnum;
import com.ferrientregas.deliverystatus.DeliveryStatusEntity;
import com.ferrientregas.deliverystatus.DeliveryStatusRepository;
import com.ferrientregas.paymenttype.PaymentTypeEntity;
import com.ferrientregas.paymenttype.PaymentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CreateFirstInstanceDeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryStatusRepository deliveryStatusRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private static final String DELIVERY_STATUS = DeliveryStatusEnum.PENDIENTE.getValue();
    private static final String PAYMENT_TYPE = PaymentTypeEnum.PAGADO.getValue();

    public DeliveryResponse create() {
        DeliveryEntity delivery = DeliveryEntity.builder()
                .numeration(generateDeliveryNumeration())
                .deliveryDate(LocalDate.now())
                .estimateHourInit(LocalTime.of(8, 0))
                .estimateHourEnd(LocalTime.of(9, 0))
                .deliveryStatus(generateDeliveryStatusEntity())
                .paymentType(generatePaymentTypeEntity())
                .evidence(new ArrayList<>())
                .credit(BigDecimal.ZERO)
                .total(BigDecimal.ZERO)
                .build();

        return DeliveryMapper.toDeliveryResponse(delivery);
    }

    /*** Another methods ***/

    private String generateDeliveryNumeration() {
        long sizeOfDelivery = this.deliveryRepository.count();

        return String.valueOf(sizeOfDelivery + 1);
    }

    private DeliveryStatusEntity generateDeliveryStatusEntity() {
        return this.deliveryStatusRepository.findByName(DELIVERY_STATUS)
                .orElseGet(() -> this.deliveryStatusRepository.save(
                        new DeliveryStatusEntity(DELIVERY_STATUS)
                ));
    }

    private PaymentTypeEntity generatePaymentTypeEntity() {
        return this.paymentTypeRepository.findByName(PAYMENT_TYPE)
                .orElseGet(() -> this.paymentTypeRepository.save(
                        new PaymentTypeEntity(PAYMENT_TYPE)
                ));
    }

}
