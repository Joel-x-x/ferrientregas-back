package com.ferrientregas.delivery.service;

import com.ferrientregas.customer.CustomerEntity;
import com.ferrientregas.customer.CustomerRepository;
import com.ferrientregas.delivery.DeliveryEntity;
import com.ferrientregas.delivery.dto.DeliveryRequest;
import com.ferrientregas.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.time.LocalTime.now;

@Component
@RequiredArgsConstructor
public class CreateDeliveryService {

    private final CreateEntitiesDefaultService createEntitiesDefaultService;
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
                        createEntitiesDefaultService.getOrCreateDeliveryStatus(deliveryRequest.deliveryStatus())
                )
                .paymentType(
                        createEntitiesDefaultService.getOrCreatePaymentType(deliveryRequest.paymentType())
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
