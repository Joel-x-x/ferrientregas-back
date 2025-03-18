package com.ferrientregas.customer.dto;

import com.ferrientregas.customer.CustomerEntity;

public class CustomerMapper {
    public static CustomerResponse toCustomerResponse(CustomerEntity customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstNames(),
                customer.getLastNames(),
                customer.getIdentification(),
                customer.getAddress(),
                customer.getAddressMaps(),
                customer.getPhone(),
                customer.getBirthDate(),
                customer.getEmail()
        );
    }
}
