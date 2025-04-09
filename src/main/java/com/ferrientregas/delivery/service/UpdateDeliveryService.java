package com.ferrientregas.delivery.service;

import com.ferrientregas.customer.CustomerRepository;
import com.ferrientregas.delivery.DeliveryEntity;
import com.ferrientregas.delivery.DeliveryRepository;
import com.ferrientregas.delivery.dto.DeliveryUpdateRequest;
import com.ferrientregas.delivery.rules.DeliveryBusinessLogicValidations;
import com.ferrientregas.role.RoleEntity;
import com.ferrientregas.user.UserRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.time.LocalTime.now;

@Component
@RequiredArgsConstructor
public class UpdateDeliveryService {

    private final CreateEntitiesDefaultService createEntitiesDefaultService;
    private final DeliveryRepository deliveryRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final List<DeliveryBusinessLogicValidations> deliveryBusinessLogicValidations;

    public DeliveryEntity update(DeliveryUpdateRequest request, UUID id) {

        DeliveryEntity delivery = this.deliveryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Delivery not found"));

        if(!StringUtils.isBlank(request.invoiceNumber())) {
            delivery.setInvoiceNumber(request.invoiceNumber());
        }
        if(request.deliveryDate() != null) {
            delivery.setDeliveryDate(request.deliveryDate());
        }
        if(request.estimateHourInit() != null) {
            delivery.setEstimateHourInit(request.estimateHourInit());
        }
        // TODO: FUNCIONALIDAD DE QUE CAMBIE A ATRASADO
        if(request.estimateHourEnd() != null) {
            delivery.setEstimateHourEnd(request.estimateHourEnd());
        }
        if(request.deliveryStatus() != null) {
            delivery.setDeliveryStatus(this.createEntitiesDefaultService.
                    getOrCreateDeliveryStatus(request.deliveryStatus()));
        }
        if(request.paymentType() != null) {
            delivery.setPaymentType(this.createEntitiesDefaultService
                    .getOrCreatePaymentType(request.paymentType()));
        }
        if(request.credit() != null) {
            delivery.setCredit(request.credit());
        }
        if(request.total() != null) {
            delivery.setTotal(request.total());
        }
        if(request.userId() != null) {
            delivery.setUser(
                    this.userRepository.findById(request.userId())
                            .orElseThrow(() -> new EntityNotFoundException("User not found"))
            );
        }
        if(request.customerId() != null) {
            delivery.setCustomer(
                    this.customerRepository.findById(request.customerId())
                            .orElseThrow(() -> new EntityNotFoundException("Customer not found"))
            );
        }
        if(!StringUtils.isBlank(request.deliveryData())) {
            delivery.setDeliveryData(request.deliveryData());
        }
        if(!StringUtils.isBlank(request.observations())) {
            delivery.setObservations(request.observations());
        }
        if(!StringUtils.isBlank(request.comments())) {
            delivery.setComments(request.comments());
        }

        // Bussines logic validations
        deliveryBusinessLogicValidations.forEach(
                validation -> {
                    validation.validateInitHour(delivery);
                }
        );

        return delivery;
    }

    /** Antoher methods **/

//    private Set<RoleEntity> getOrCreateRole(String role){
//        return new HashSet<>(Collections.singleton(
//                this.roleRepository.findByName(role)
//                        .orElseGet(() ->
//                                ROLES.stream()
//                                        .filter(r -> r.equals(role))
//                                        .findFirst()
//                                        .map(r -> roleRepository.save(new RoleEntity(r)))
//                                        .orElseThrow(() -> new EntityNotFoundException("Role not found")
//                                        )
//                        )));
//    }
}
