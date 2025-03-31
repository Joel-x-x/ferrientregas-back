package com.ferrientregas.delivery.utils;

import com.ferrientregas.customer.CustomerEntity;
import com.ferrientregas.customer.CustomerRepository;
import com.ferrientregas.delivery.DeliveryEntity;
import com.ferrientregas.delivery.DeliveryRepository;
import com.ferrientregas.delivery.dto.DeliveryRequest;
import com.ferrientregas.delivery.rules.DeliveryBusinessLogicValidations;
import com.ferrientregas.deliverystatus.DeliveryStatusEntity;
import com.ferrientregas.deliverystatus.DeliveryStatusRepository;
import com.ferrientregas.paymenttype.PaymentTypeEntity;
import com.ferrientregas.paymenttype.PaymentTypeRepository;
import com.ferrientregas.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static java.time.LocalTime.now;

@Component
@RequiredArgsConstructor
public class DeliveryFactory {

    private final DeliveryStatusRepository deliveryStatusRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final DeliveryRepository deliveryRepository;
    private static final Set<String> DELIVERY_STATUS = Set.of("PENDIENTE", "EN-PROCESO",
            "INCOMPLETO", "ENTREGADO", "ATRASADO", "CANCELADO");
    private static final Set<String> PAYMENT_TYPE = Set.of("CREDITO", "CONTRA-ENTREGA",
            "INCOMPLETO", "PAGADO", "ABONADO");
    private final List<DeliveryBusinessLogicValidations> deliveryBusinessLogicValidations;

    public DeliveryEntity createDelivery(DeliveryRequest deliveryRequest) {

        CustomerEntity customer = customerRepository.findById(
                                deliveryRequest.customerId()
                        )

        return DeliveryEntity.builder()
                .numeration(deliveryRequest.numeration())
                .invoiceNumber(deliveryRequest.invoiceNumber())
                .deliveryDate(deliveryRequest.deliveryDate())
                .estimateHourInit(deliveryRequest.estimateHourInit())
                .estimateHourEnd(deliveryRequest.estimateHourEnd())
                .deliveryStatus(
                        getOrCreateDeliveryStatus(deliveryRequest.deliveryStatus())
                )
                .paymentType(
                        getOrCreatePaymentType(deliveryRequest.paymentType())
                )
                .credit(deliveryRequest.credit())
                .total(deliveryRequest.total())
                .evidence(deliveryRequest.evidence())
                .user(
                        userRepository.findById(
                                deliveryRequest.userId()
                        ).orElseThrow(
                                () -> new EntityNotFoundException("User not found")
                        )
                )
//                .customer(
//                        .orElse()
//                )
                .deliveryData(deliveryRequest.deliveryData())
                .observations(deliveryRequest.observations())
                .comments(deliveryRequest.comments())
                .build();
    }

    /*** Another Methods ***/

    private DeliveryStatusEntity getOrCreateDeliveryStatus(String deliveryStatus){
        return this.deliveryStatusRepository.findByName(deliveryStatus)
                        .orElseGet(() ->
                                DELIVERY_STATUS.stream()
                                        .filter(d -> d.equals(deliveryStatus))
                                        .findFirst()
                                        .map(d -> deliveryStatusRepository.save(
                                                new DeliveryStatusEntity(d)))
                                        .orElseThrow(() -> new EntityNotFoundException("Delivery status not found")
                                        )
                        );
    }

    private PaymentTypeEntity getOrCreatePaymentType(String paymentType){
        return this.paymentTypeRepository.findByName(paymentType)
                        .orElseGet(() ->
                                PAYMENT_TYPE.stream()
                                        .filter(p -> p.equals(paymentType))
                                        .findFirst()
                                        .map(p -> paymentTypeRepository.save(
                                                new PaymentTypeEntity(p)))
                                        .orElseThrow(() -> new EntityNotFoundException("Payment type not found")
                                        )
                        );
    }

    // TODO: TAKE ACCOUNT THE DRIVER TO CALCULATE ESTIMATE HOUR
//    private LocalTime calculateEstimateHourInit(DeliveryRequest deliveryRequest) {
//        List<DeliveryEntity> pendingDeliveries =
//                deliveryRepository.findPendingDeliveriesTodayOrderByEstimateHourInit();
//
//        // Workable Hours
//        deliveryBusinessLogicValidations.forEach(
//                validation -> {
//                    validation.validateInitHour(deliveryRequest.DeliveryDate(),
//                            pendingDeliveries);
//                }
//        );
//        return now();
//    }
}
