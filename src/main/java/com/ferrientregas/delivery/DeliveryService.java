package com.ferrientregas.delivery;

import com.ferrientregas.deliverystatus.DeliveryStatusRepository;
import com.ferrientregas.paymenttype.PaymentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static java.time.LocalTime.now;

@Service
@RequiredArgsConstructor
public class DeliveryService {
   private final DeliveryRepository deliveryRepository;
   private final DeliveryStatusRepository deliveryStatusRepository;
   private final PaymentTypeRepository paymentTypeRepository;

   public DeliveryResponse createDelivery(DeliveryRequest deliveryRequest) {
       DeliveryEntity delivery =
               this.deliveryRepository.save(DeliveryEntity.builder()
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
                       .user(deliveryRequest.user())
                       .customer(deliveryRequest.customer())
                       .deliveryData(deliveryRequest.deliveryData())
                       .observations(deliveryRequest.observations())
                       .comments(deliveryRequest.comments())
                       .build()
               );
       return new DeliveryResponse(
               delivery.getNumeration(),
               delivery.getInvoiceNumber(),
               delivery.getDeliveryDate(),
               delivery.getEstimateHourInit(),
               delivery.getEstimateHourEnd(),
               delivery.getDeliveryStatus(),
               delivery.getPaymentType(),
               delivery.getCredit(),
               delivery.getTotal(),
               delivery.getEvidence(),
               delivery.getUser(),
               delivery.getCustomer(),
               delivery.getDeliveryData(),
               delivery.getObservations(),
               delivery.getComments()
       );
   }


}
