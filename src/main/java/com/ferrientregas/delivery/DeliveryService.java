package com.ferrientregas.delivery;

import com.ferrientregas.delivery.dto.*;
import com.ferrientregas.delivery.service.CreateFirstInstanceDeliveryService;
import com.ferrientregas.delivery.service.CreateDeliveryService;
import com.ferrientregas.delivery.utils.DeliveryMapper;
import com.ferrientregas.delivery.service.UpdateDeliveryService;
import com.ferrientregas.evidence.EvidenceEntity;
import com.ferrientregas.evidence.EvidenceService;
import com.ferrientregas.role.utils.RoleEnum;
import com.ferrientregas.user.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

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
   private final CreateDeliveryService createDeliveryService;
   private final UpdateDeliveryService updateDeliveryService;
   private final EvidenceService evidenceService;
   private final CreateFirstInstanceDeliveryService createFirstInstanceDeliveryService;

    public DeliveryResponse getById(UUID id) {
        return deliveryRepository.findById(id)
                .map(DeliveryMapper::toDeliveryResponse)
                .orElseThrow(
                        ()-> new EntityNotFoundException("No delivery found for id:"
                                + id));
    }

    public Page<DeliveryResponse> getByUserId(Pageable pageable, UUID
                                                      userId){
        return deliveryRepository.findAllByUserIdAndDeletedIsFalse(userId, pageable)
                .map(DeliveryMapper::toDeliveryResponse);

    }

    public Page<DeliveryResponse> getAll(Pageable pageable){
        return deliveryRepository.findAllByDeletedIsFalse(pageable)
                .map(DeliveryMapper::toDeliveryResponse);
    }

    /**
     * Get all delivery by status, take account in case the user be a Driver
     * return all driver deliveries, but the user is an Admin return all deliveries
     *
     * @param pageable Pageable
     * @param authentication Authentication to know the user logged
     * @param deliveryStatus DeliveryStatus to filter the list
     * @return List<DeliveryStatus>
     */
    // TODO: REFACTORING WITH SPECIFICATION API
    public Page<DeliveryResponse> getAllByDeliveryStatus(Pageable pageable, Authentication authentication, String deliveryStatus) {
        UserEntity user = (UserEntity) authentication.getPrincipal();

        boolean isAdmin = user.getRoles().stream().anyMatch(role ->
                role.getName().equals(RoleEnum.ADMIN.getValue()) ||
                        role.getName().equals(RoleEnum.EMPLOYEE.getValue()));

        if(isAdmin) {
            return deliveryRepository.findAllByDeletedIsFalseAndDeliveryStatus_Name(pageable, deliveryStatus)
                    .map(DeliveryMapper::toDeliveryResponse);
        } else {
            return deliveryRepository.findAllByDeletedIsFalseAndUserAndDeliveryStatus_Name(pageable, user, deliveryStatus)
                    .map(DeliveryMapper::toDeliveryResponse);
        }
    }

    public DeliveryResponse createDelivery(DeliveryRequest request) {
        DeliveryEntity delivery = deliveryRepository.save(
                createDeliveryService.createDelivery(request)
        );

        // Create evidences
        List<EvidenceEntity> evidence = this.evidenceService.createAll(request.evidence(), delivery);

        delivery.setEvidence(evidence);

        return DeliveryMapper.toDeliveryResponse(delivery);
    }

    /**
     * Create first instance of delivery,
     * this method generate all data that needs to create a generic delivery but no persist the entity
     * generate id, date, estimated hour init, estimated hour end, delivery status
     * and payment type.
     * @return DeliveryEntity
     */
   public DeliveryResponse createFirst() {
       return createFirstInstanceDeliveryService.create();
   }

    public DeliveryResponse updateDelivery(
            DeliveryUpdateRequest request,
            UUID id
    ){
        DeliveryEntity delivery = updateDeliveryService.update(request, id);
        deliveryRepository.save(delivery);
        return DeliveryMapper.toDeliveryResponse(delivery);
    }

   public void deleteDelivery(UUID id) {
      DeliveryEntity delivery = getDeliveryById(id);
      delivery.setDeleted(true);
      deliveryRepository.save(delivery);
   }


   /** Another Methods **/

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
