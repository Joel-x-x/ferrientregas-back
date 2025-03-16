package com.ferrientregas.deliverystatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryStatusRepository extends JpaRepository<DeliveryStatusEntity, UUID> {
    List<DeliveryStatusEntity> findAllByDeletedFalse();
}
