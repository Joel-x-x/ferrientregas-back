package com.ferrientregas.paymenttype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentTypeRepository extends JpaRepository<PaymentTypeEntity, UUID> {
    List<PaymentTypeEntity> findAllByDeletedFalse();
    PaymentTypeEntity findByName(String name);
}
