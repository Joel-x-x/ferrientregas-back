package com.ferrientregas.delivery;

import com.ferrientregas.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryRepository extends JpaRepository<DeliveryEntity,
        UUID> {

    /** getAll **/
    Page<DeliveryEntity> findAllByDeletedIsFalseAndUserAndDeliveryStatus_Name(Pageable pageable, UserEntity user, String name);
    Page<DeliveryEntity> findAllByDeletedIsFalseAndDeliveryStatus_Name(Pageable pageable, String name);

    Page<DeliveryEntity> findAllByDeletedIsFalse(Pageable pageable);

    @Query("SELECT d FROM DeliveryEntity d WHERE d.deliveryDate = CURRENT_DATE " +
            "AND d.deliveryStatus.name = 'PENDIENTE' ORDER BY d.estimateHourInit")
    List<DeliveryEntity> findPendingDeliveriesTodayOrderByEstimateHourInit();


    Page<DeliveryEntity> findAllByUserIdAndDeletedIsFalse(UUID userId,
                                                          Pageable pageable);

    /** Reports **/
    long countByDeliveryStatus_Name(String status);
    long countByPaymentType_Name(String payment);
    long countByDeliveryStatus_NameAndPaymentType_Name(String status, String payment);
}
