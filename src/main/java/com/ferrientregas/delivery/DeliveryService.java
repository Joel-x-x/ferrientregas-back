package com.ferrientregas.delivery;

import com.ferrientregas.delivery.dto.*;
import com.ferrientregas.delivery.service.CreateFirstInstanceDeliveryService;
import com.ferrientregas.delivery.utils.DeliveryFactory;
import com.ferrientregas.delivery.utils.DeliveryMapper;
import com.ferrientregas.delivery.utils.DeliveryUpdater;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.time.LocalTime.now;

@Service
@RequiredArgsConstructor
public class DeliveryService {
   private final DeliveryRepository deliveryRepository;
   private final DeliveryFactory deliveryFactory;
   private final DeliveryUpdater deliveryUpdater;
   private final CreateFirstInstanceDeliveryService createFirstInstanceDeliveryService;

    public DeliveryResponse getDelivery(UUID id) {
        return deliveryRepository.findById(id)
                .map(DeliveryMapper::toDeliveryResponse)
                .orElseThrow(
                        ()-> new EntityNotFoundException("No delivery found for id:"
                                + id));
    }

    public Page<DeliveryResponse> getDeliveryByUserId(Pageable pageable, UUID
                                                      userId){
        return deliveryRepository.findAllByUserIdAndDeletedIsFalse(userId, pageable)
                .map(DeliveryMapper::toDeliveryResponse);

    }

    public Page<DeliveryResponse> listDelivery(Pageable pageable){
        return deliveryRepository.findAllByDeletedIsFalse(pageable)
                .map(DeliveryMapper::toDeliveryResponse);
    }

   public DeliveryResponse createDelivery(DeliveryRequest deliveryRequest) {
       DeliveryEntity delivery = deliveryFactory.createDelivery(deliveryRequest);
       deliveryRepository.save(delivery);
       return DeliveryMapper.toDeliveryResponse(delivery);
   }

    /***
     * Create first instance of delivery
     * This method generate all data that the front needs but no persist the entity
     * generate id, date, hour estimated init, hour estimated end, delivery status
     * and payment type.
     * @return DeliveryEntity
     */
   public DeliveryResponse createFirst() {
       return createFirstInstanceDeliveryService.create();
   }

    public DeliveryResponse updateDelivery(UUID id,
            DeliveryUpdateRequest deliveryRequest
    ){
       DeliveryEntity delivery = getDeliveryById(id);
        deliveryUpdater.updateDeliveryFields(delivery, deliveryRequest);
        deliveryRepository.save(delivery);
        return DeliveryMapper.toDeliveryResponse(delivery);
    }

   public void deleteDelivery(UUID id) {
      DeliveryEntity delivery = getDeliveryById(id);
      delivery.setDeleted(true);
      deliveryRepository.save(delivery);
   }

   /*** Another Methods ***/

   public Optional<Map<String, LocalTime>> getNextAvailableHours(){

       LocalTime morningStartTime = LocalTime.of(8, 0);
       LocalTime nightEndTime = LocalTime.of(18,0);

       List<DeliveryEntity> deliveries =
               deliveryRepository.findPendingDeliveriesTodayOrderByEstimateHourInit();

       LocalTime possibleStart = morningStartTime;

       for(DeliveryEntity delivery : deliveries){
           if(possibleStart.plusHours(1).isBefore(delivery
                   .getEstimateHourInit())|| possibleStart.plusHours(1)
                   .equals(delivery.getEstimateHourInit())
           ){
               return Optional.of(Map.of(
                       "estimatedStart", possibleStart,
                       "estimatedEnd", possibleStart.plusHours(1)
               ));
           }

           if(delivery.getEstimateHourEnd().isAfter(possibleStart)){
               possibleStart = delivery.getEstimateHourEnd();
           }
       }

       if(possibleStart.plusHours(1).isBefore(nightEndTime)||
       possibleStart.plusHours(1).equals(nightEndTime)){
           return Optional.of(Map.of(
                   "estimatedStart", possibleStart,
                   "estimatedEnd", possibleStart.plusHours(1)
           ));
       }

       return Optional.empty();
   }

   private DeliveryEntity getDeliveryById(UUID id){
        return deliveryRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("No delivery " +
                        "found for id: " + id));
   }

}
