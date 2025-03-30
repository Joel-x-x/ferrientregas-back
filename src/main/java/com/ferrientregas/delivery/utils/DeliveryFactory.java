package com.ferrientregas.delivery.utils;

import com.ferrientregas.customer.CustomerRepository;
import com.ferrientregas.delivery.DeliveryEntity;
import com.ferrientregas.delivery.DeliveryRepository;
import com.ferrientregas.delivery.dto.DeliveryRequest;
import com.ferrientregas.delivery.rules.DeliveryBusinessLogicValidations;
import com.ferrientregas.deliverystatus.DeliveryStatusEntity;
import com.ferrientregas.deliverystatus.DeliveryStatusRepository;
import com.ferrientregas.paymenttype.PaymentTypeRepository;
import com.ferrientregas.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

import static java.time.LocalTime.now;

@Component
@RequiredArgsConstructor
public class DeliveryFactory {

    private final DeliveryStatusRepository deliveryStatusRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final DeliveryRepository deliveryRepository;
    private static final String DELIVERY_STATUS = "PENDIENTE";
    private final List<DeliveryBusinessLogicValidations> deliveryBusinessLogicValidations;

    public DeliveryEntity createDelivery(DeliveryRequest deliveryRequest) {
        return DeliveryEntity.builder()
                .numeration(deliveryRequest.numeration())
                .invoiceNumber(deliveryRequest.invoiceNumber())
                .deliveryDate(deliveryRequest.DeliveryDate())
                .estimateHourInit(calculateEstimateHourInit(deliveryRequest))
                .estimateHourEnd(deliveryRequest.estimateHourEnd().plusHours(1))
                .deliveryStatus(
                        getOrCreateDeliveryStatus()
                )
//                .paymentType(
//                        paymentTypeRepository.findByName(
//                                deliveryRequest.paymentType()
//                        )
//                )
                .credit(deliveryRequest.credit())
                .total(deliveryRequest.total())
                .evidence(deliveryRequest.evidence())
                .user(
                        userRepository.findUserEntityById(
                                deliveryRequest.user()
                        )
                )
                .customer(
                        customerRepository.findCustomerEntityById(
                                deliveryRequest.customer()
                        )
                )
                .deliveryData(deliveryRequest.deliveryData())
                .observations(deliveryRequest.observations())
                .comments(deliveryRequest.comments())
                .build();
    }

    private DeliveryStatusEntity getOrCreateDeliveryStatus() {
        return this.deliveryStatusRepository.findByName(DELIVERY_STATUS)
                .orElseGet(() -> this.deliveryStatusRepository.save(
                        DeliveryStatusEntity.builder()
                                .name(DELIVERY_STATUS)
                                .build()
                ));
    }

    private LocalTime calculateEstimateHourInit(DeliveryRequest deliveryRequest) {
        List<DeliveryEntity> pendingDeliveries =
                deliveryRepository.findPendingDeliveriesTodayOrderByEstimateHourInit();

        // Workable Hours
        deliveryBusinessLogicValidations.forEach(
                validation -> {
                    validation.validateInitHour(deliveryRequest.DeliveryDate(),
                            pendingDeliveries);
                }
        );
        return now();
    }
}
