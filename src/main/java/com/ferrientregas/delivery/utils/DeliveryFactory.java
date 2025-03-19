package com.ferrientregas.delivery.utils;

import com.ferrientregas.customer.CustomerRepository;
import com.ferrientregas.delivery.DeliveryEntity;
import com.ferrientregas.delivery.DeliveryRepository;
import com.ferrientregas.delivery.dto.DeliveryRequest;
import com.ferrientregas.deliverystatus.DeliveryStatusRepository;
import com.ferrientregas.paymenttype.PaymentTypeRepository;
import com.ferrientregas.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
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

    /*
     * TODO: Create the logic for making the hourInit be
     *  automatic comparing it with the other existing deliveries
     *  by default the hourEnd will be hourInit +1hour and it can
     *  be updatable, the hourInit has to take the the deliveryDate
     *  and get the first workable hour of the day if it isn't on
     *  current working day, if the estimate hourEnd get over the
     *  time adds 30 minutes and if it gets late then it would be
     *  marked as overdue.
     */

    /*
     * TODO: If a delivery it's done, it have to be removed from the list of
     *  the undelivered ones.
     */

    /*
     * TODO: If a delivery can be taken before the estimate hourInit, when
     *  it's took by a driver, I need to get the hour when the delivery has
     *  been taken and reassign the hourInit, so for making this possible I
     *  have to create another field to set the delivered hour.
     */

    /*
     * TODO: I have to define the workable hours, let's say the following ones:
     *  Morning Hours: 8AM - 12PM
     *  AfterNoon Hours: 13PM- 22PM
     *  those are the available hours when a delivery can be done, if it
     *  exist a delivery after 22PM it will be reassigned to the other day first
     *  workable hour.
     */

    public DeliveryEntity createDelivery(DeliveryRequest deliveryRequest) {
        return DeliveryEntity.builder()
                .numeration(deliveryRequest.numeration())
                .invoiceNumber(deliveryRequest.invoiceNumber())
                .deliveryDate(deliveryRequest.DeliveryDate())
                .estimateHourInit(now())
                .estimateHourEnd(null)
                .deliveryStatus(
                        deliveryStatusRepository.findByName(
                                deliveryRequest.deliveryStatusName()
                        )
                )
                .paymentType(
                        paymentTypeRepository.findByName(
                                deliveryRequest.paymentType()
                        )
                )
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

//    private LocalTime calculateEstimateHourInit() {
//        List<DeliveryEntity> pendingDeliveries =
//                deliveryRepository.findAllByDeliveryStatusName("PENDIENTE");
//
//
//    }
}
