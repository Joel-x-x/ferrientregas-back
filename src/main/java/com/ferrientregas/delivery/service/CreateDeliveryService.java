package com.ferrientregas.delivery.service;

import com.ferrientregas.customer.CustomerEntity;
import com.ferrientregas.customer.CustomerRepository;
import com.ferrientregas.delivery.DeliveryEntity;
import com.ferrientregas.delivery.dto.DeliveryRequest;
import com.ferrientregas.delivery.utils.DeliveryStatusEnum;
import com.ferrientregas.delivery.utils.PaymentTypeEnum;
import com.ferrientregas.deliverystatus.DeliveryStatusEntity;
import com.ferrientregas.deliverystatus.DeliveryStatusRepository;
import com.ferrientregas.paymenttype.PaymentTypeEntity;
import com.ferrientregas.paymenttype.PaymentTypeRepository;
import com.ferrientregas.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.time.LocalTime.now;

@Component
@RequiredArgsConstructor
public class CreateDeliveryService {

    private final DeliveryStatusRepository deliveryStatusRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    public DeliveryEntity createDelivery(DeliveryRequest deliveryRequest) {

        CustomerEntity customer = deliveryRequest.customerId() != null
                ? customerRepository.findById(deliveryRequest.customerId()).orElse(null)
                : null;

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
                .customer(customer)
                .deliveryData(deliveryRequest.deliveryData())
                .observations(deliveryRequest.observations())
                .comments(deliveryRequest.comments())
                .build();
    }

    /*** Another Methods ***/

    private DeliveryStatusEntity getOrCreateDeliveryStatus(String deliveryStatus) {
        return this.deliveryStatusRepository.findByName(deliveryStatus)
                .orElseGet(() -> {
                    if (DeliveryStatusEnum.isValid(deliveryStatus)) {
                        return deliveryStatusRepository.save(new DeliveryStatusEntity(deliveryStatus));
                    } else {
                        throw new EntityNotFoundException("Delivery status not found");
                    }
                });
    }

    private PaymentTypeEntity getOrCreatePaymentType(String paymentType) {
        return this.paymentTypeRepository.findByName(paymentType)
                .orElseGet(() -> {
                    if (PaymentTypeEnum.isValid(paymentType)) {
                        return paymentTypeRepository.save(new PaymentTypeEntity(paymentType));
                    } else {
                        throw new EntityNotFoundException("Payment type not found");
                    }
                });
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
