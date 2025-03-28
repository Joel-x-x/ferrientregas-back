package com.ferrientregas.delivery;

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

    Optional<DeliveryEntity> findById(UUID id);
    Page<DeliveryEntity> findAllByDeletedIsFalse(Pageable pageable);
    List<DeliveryEntity> findAllByDeliveryStatusName(String pendiente);

    @Query("SELECT d FROM DeliveryEntity d WHERE CAST(d.deliveryDate AS DATE)" +
            " = CURRENT_DATE AND d.deliveryStatus = 'PENDIENTE' ORDER BY d" +
            ".estimateHourInit")
    List<DeliveryEntity> findTodayDeliveries();
}
