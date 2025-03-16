package com.ferrientregas.customer;

<<<<<<< HEAD
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
=======
import jakarta.validation.constraints.NotNull;
>>>>>>> e21ad5feaae6255098a1f93a22d050fab964bc04
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID> {
<<<<<<< HEAD
    Page<CustomerEntity> findAllByDeletedIsFalse(Pageable pageable);
=======
    CustomerEntity findCustomerEntityById( UUID customer);
>>>>>>> e21ad5feaae6255098a1f93a22d050fab964bc04
}
