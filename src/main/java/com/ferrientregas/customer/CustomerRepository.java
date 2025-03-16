package com.ferrientregas.customer;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID> {
    CustomerEntity findCustomerEntityById( UUID customer);
}
