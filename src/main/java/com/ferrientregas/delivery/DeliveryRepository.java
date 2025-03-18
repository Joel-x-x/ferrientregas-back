package com.ferrientregas.delivery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryRepository extends JpaRepository<DeliveryEntity,
        UUID> {
    Optional<DeliveryEntity> findById(UUID id);
    Page<DeliveryEntity> findAllByDeletedIsFalse(Pageable pageable);

}
