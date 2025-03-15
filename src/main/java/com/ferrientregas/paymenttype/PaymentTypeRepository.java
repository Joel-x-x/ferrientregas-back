package com.ferrientregas.paymenttype;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentTypeRepository extends JpaRepository<PaymentTypeEntity, UUID> {
    List<PaymentTypeEntity> findAllByDeletedFalse();
}
