package com.ferrientregas.delivery.service;

import com.ferrientregas.customer.CustomerEntity;
import com.ferrientregas.customer.CustomerRepository;
import com.ferrientregas.delivery.DeliveryEntity;
import com.ferrientregas.delivery.DeliveryRepository;
import com.ferrientregas.delivery.dto.DeliveryRequest;
import com.ferrientregas.evidence.EvidenceEntity;
import com.ferrientregas.evidence.EvidenceRepository;
import com.ferrientregas.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.time.LocalTime.now;

@Component
@RequiredArgsConstructor
public class CreateDeliveryService {

    private final CreateEntitiesDefaultService createEntitiesDefaultService;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final EvidenceRepository evidenceRepository;

    public DeliveryEntity createDelivery(DeliveryRequest request) {

        CustomerEntity customer = request.customerId() != null
                ? customerRepository.findById(request.customerId()).orElse(null)
                : null;

        return DeliveryEntity.builder()
                .numeration(request.numeration())
                .invoiceNumber(request.invoiceNumber())
                .deliveryDate(request.deliveryDate())
                .estimateHourInit(request.estimateHourInit())
                .estimateHourEnd(request.estimateHourEnd())
                .deliveryStatus(
                        createEntitiesDefaultService.getOrCreateDeliveryStatus(request.deliveryStatus())
                )
                .paymentType(
                        createEntitiesDefaultService.getOrCreatePaymentType(request.paymentType())
                )
                .credit(request.credit())
                .total(request.total())
                .user(
                        userRepository.findById(
                                request.userId()
                        ).orElseThrow(
                                () -> new EntityNotFoundException("User not found")
                        )
                )
                .customer(customer)
                .deliveryData(request.deliveryData())
                .observations(request.observations())
                .comments(request.comments())
                .build();
    }


    // TODO: TAKE ACCOUNT THE DRIVER TO CALCULATE ESTIMATE HOUR
//    private LocalTime calculateEstimateHourInit(DeliveryRequest request) {
//        List<DeliveryEntity> pendingDeliveries =
//                deliveryRepository.findPendingDeliveriesTodayOrderByEstimateHourInit();
//
//        // Workable Hours
//        deliveryBusinessLogicValidations.forEach(
//                validation -> {
//                    validation.validateInitHour(request.DeliveryDate(),
//                            pendingDeliveries);
//                }
//        );
//        return now();
//    }
}
