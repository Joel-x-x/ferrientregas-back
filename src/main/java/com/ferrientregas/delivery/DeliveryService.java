package com.ferrientregas.delivery;

import com.ferrientregas.customer.CustomerRepository;
import com.ferrientregas.delivery.dto.DeliveryMapper;
import com.ferrientregas.delivery.dto.DeliveryRequest;
import com.ferrientregas.delivery.dto.DeliveryResponse;
import com.ferrientregas.delivery.dto.DeliveryUpdateRequest;
import com.ferrientregas.delivery.exception.DeliveryNotFoundException;
import com.ferrientregas.deliverystatus.DeliveryStatusRepository;
import com.ferrientregas.paymenttype.PaymentTypeRepository;
import com.ferrientregas.user.UserRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.time.LocalTime.now;

@Service
@RequiredArgsConstructor
public class DeliveryService {
   private final DeliveryRepository deliveryRepository;
   private final DeliveryStatusRepository deliveryStatusRepository;
   private final PaymentTypeRepository paymentTypeRepository;
   private final UserRepository userRepository;
   private final CustomerRepository customerRepository;

    public DeliveryResponse getDelivery(UUID id) throws DeliveryNotFoundException {
        return this.deliveryRepository.findById(id)
                .map(DeliveryMapper::toDeliveryResponse)
                .orElseThrow(DeliveryNotFoundException::new);
    }

    public Page<DeliveryResponse> listDelivery(Pageable pageable){
        return this.deliveryRepository.findAllByDeletedIsFalse(pageable)
                .map(DeliveryMapper::toDeliveryResponse);
    }

   public DeliveryResponse createDelivery(DeliveryRequest deliveryRequest) {
       DeliveryEntity delivery = createAndSaveDelivery(deliveryRequest);
       return DeliveryMapper.toDeliveryResponse(delivery);
   }

    public DeliveryResponse updateDelivery(UUID id,
                                           DeliveryUpdateRequest deliveryRequest)
            throws DeliveryNotFoundException {
       DeliveryEntity delivery = deliveryRepository.findById(id)
               .orElseThrow(DeliveryNotFoundException::new);
        updateDeliveryFields(delivery, deliveryRequest);
        return DeliveryMapper.toDeliveryResponse(delivery);
    }

   public Boolean deleteDelivery(UUID id) throws DeliveryNotFoundException {
      DeliveryEntity delivery = this.deliveryRepository.findById(id)
              .orElseThrow(DeliveryNotFoundException::new);
      delivery.setDeleted(true);
      this.deliveryRepository.save(delivery);

      return true;
   }

   private DeliveryEntity createAndSaveDelivery(DeliveryRequest deliveryRequest) {
       return this.deliveryRepository.save(DeliveryEntity.builder()
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
               .build()
       );
   }

   private void updateDeliveryFields(DeliveryEntity delivery,
                                     DeliveryUpdateRequest deliveryRequest) {

       if(!StringUtils.isBlank(deliveryRequest.numeration())) {
           delivery.setNumeration(deliveryRequest.numeration());
       }
       if(!StringUtils.isBlank(deliveryRequest.invoiceNumber())) {
           delivery.setInvoiceNumber(deliveryRequest.invoiceNumber());
       }
       if(!StringUtils.isBlank(deliveryRequest.deliveryDate())) {
           delivery.setDeliveryDate(deliveryRequest.deliveryDate());
       }
       delivery.setEstimateHourInit(now());
       delivery.setEstimateHourEnd(now());
       delivery.setDeliveryStatus(
               this.deliveryStatusRepository.findByName(
                       deliveryRequest.deliveryStatusName()
               )
       );
       delivery.setPaymentType(
               this.paymentTypeRepository.findByName(
                       deliveryRequest.paymentType()
               )
       );
       delivery.setCredit(deliveryRequest.credit());
       delivery.setTotal(deliveryRequest.total());
       delivery.setEvidence(deliveryRequest.evidence());
       delivery.setUser(
               this.userRepository.findUserEntityById(
                       deliveryRequest.user()
               )
       );
       delivery.setCustomer(
               this.customerRepository.findCustomerEntityById(
                       deliveryRequest.customer()
               )
       );
       delivery.setDeliveryData(deliveryRequest.deliveryData());
       delivery.setObservations(deliveryRequest.observations());
       delivery.setComments(deliveryRequest.comments());
   }


}
