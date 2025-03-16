package com.ferrientregas.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID> {
    Page<CustomerEntity> findAllByDeletedIsFalse(Pageable pageable);
    CustomerEntity findCustomerEntityById( UUID customer);
}
