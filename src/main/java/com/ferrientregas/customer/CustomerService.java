package com.ferrientregas.customer;

import com.ferrientregas.customer.dto.CustomerMapper;
import com.ferrientregas.customer.dto.CustomerRequest;
import com.ferrientregas.customer.dto.CustomerResponse;
import com.ferrientregas.customer.dto.CustomerUpdateRequest;
import com.ferrientregas.customer.exception.CustomerNotFoundException;
import com.ferrientregas.customer.utils.PasswordGenerator;
import com.ferrientregas.role.RoleEntity;
import com.ferrientregas.role.RoleRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String CUSTOMER_ROLE = "CUSTOMER";

    public Page<CustomerResponse> list(Pageable pageable) {
        return this.customerRepository.findAllByDeletedIsFalse(pageable)
                .map(CustomerMapper::toCustomerResponse);
    }

    public CustomerResponse get(UUID id){
        return this.customerRepository.findById(id)
                .map(CustomerMapper::toCustomerResponse)
                .orElseThrow(()->
                        new EntityNotFoundException("Customer not found"));

    }

    public CustomerResponse create(CustomerRequest request) {
        RoleEntity role = getOrCreateRole();
        Set<RoleEntity> roles = Collections.singleton(role);
        CustomerEntity customer = createAndSaveCustomer(roles,request);

        return CustomerMapper.toCustomerResponse(customer);
    }

    public CustomerResponse update(CustomerUpdateRequest request, UUID id) {
        CustomerEntity customer = getCustomerById(id);
        updateCustomerFields(customer,request);

        return CustomerMapper.toCustomerResponse(customer);
    }

    public Boolean delete(UUID id) {
        CustomerEntity customer = getCustomerById(id);
        customer.setDeleted(true);
        this.customerRepository.save(customer);

        return true;
    }



    private CustomerEntity getCustomerById(UUID id) {
        return this.customerRepository.findById(id)
                .orElseThrow(()->
                        new EntityNotFoundException("Customer not found"));
    }

    private RoleEntity getOrCreateRole(){

        return this.roleRepository.findByName(CUSTOMER_ROLE)
                .orElseGet(()->roleRepository.save(RoleEntity.builder()
                        .name(CUSTOMER_ROLE).build()));
    }

    private CustomerEntity createAndSaveCustomer(Set<RoleEntity> roles,
                                                 CustomerRequest customerRequest) {

        return this.customerRepository.save(CustomerEntity.builder()
                .firstNames(customerRequest.firstNames())
                .lastNames(customerRequest.lastNames())
                .email(customerRequest.email())
                .password(passwordEncoder.encode(
                        PasswordGenerator.generatePassword()))
                .roles(roles)
                .build());
    }

    private void updateCustomerFields(CustomerEntity customer,
                                   CustomerUpdateRequest request) {
        if (!StringUtils.isBlank(request.firstNames())) {
            customer.setFirstNames(request.firstNames());
        }
        if (!StringUtils.isBlank(request.lastNames())) {
            customer.setLastNames(request.lastNames());
        }
        if (!StringUtils.isBlank(request.identification())) {
            customer.setIdentification(request.identification());
        }
        if (!StringUtils.isBlank(request.address())) {
            customer.setAddress(request.address());
        }
        if (!StringUtils.isBlank(request.addressMaps())) {
            customer.setAddressMaps(request.addressMaps());
        }
        if (!StringUtils.isBlank(request.phone())) {
            customer.setPhone(request.phone());
        }
        if (request.birthDate() != null) {
            customer.setBirthDate(request.birthDate());
        }
    }

}
