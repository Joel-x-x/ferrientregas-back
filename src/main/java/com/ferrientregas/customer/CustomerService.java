package com.ferrientregas.customer;

import com.ferrientregas.customer.dto.CustomerRequest;
import com.ferrientregas.customer.dto.CustomerResponse;
import com.ferrientregas.customer.dto.CustomerUpdateRequest;
import com.ferrientregas.customer.exception.CustomerNotFoundException;
import com.ferrientregas.customer.utils.PasswordGenerator;
import com.ferrientregas.role.RoleEntity;
import com.ferrientregas.role.RoleRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    /***
     * List customers with paginate
     *
     * @param pageable Pageable
     * @return response Page<response>
     */
    public Page<CustomerResponse> list(Pageable pageable) {
        return this.customerRepository.findAllByDeletedIsFalse(pageable)
                .map(x -> new CustomerResponse(
                        x.getId(),
                        x.getFirstNames(),
                        x.getLastNames(),
                        x.getIdentification(),
                        x.getAddress(),
                        x.getAddressMaps(),
                        x.getPhone(),
                        x.getBirthDate(),
                        x.getEmail()
                ));
    }

    /***
     * Get customer
     *
     * @param id UUID
     * @return response
     * @throws CustomerNotFoundException not found exception
     */
    public CustomerResponse get(UUID id) throws CustomerNotFoundException {
        CustomerEntity customer = this.customerRepository.findById(id)
                .orElseThrow(CustomerNotFoundException::new);

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

    /***
     * Create customer and user default with aleatory password
     *
     * @param request customer
     * @return response
     */
    public CustomerResponse create(CustomerRequest request) {

        // Get role CUSTOMER
        RoleEntity role = roleRepository.findByName("CUSTOMER")
                .orElseGet(()->roleRepository.save(RoleEntity.builder().name(
                        "CUSTOMER").build()));

        // Add role to new customer
        Set<RoleEntity> roles = Set.of(role);

        // Create only customer with user default password
        CustomerEntity customer = this.customerRepository.save(
                CustomerEntity.builder()
                        .firstNames(request.firstNames())
                        .lastNames(request.lastNames())
                        .identification(request.identification())
                        .address(request.address())
                        .addressMaps(request.addressMaps())
                        .phone(request.phone())
                        .birthDate(request.birthDate())
                        .email(request.email())
                        .password(passwordEncoder.encode(PasswordGenerator.generatePassword()))
                        .roles(roles)
                        .build()
        );

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

    /***
     * Update customer
     *
     * @param request customer
     * @param id UUID
     * @return response
     * @throws CustomerNotFoundException not found exception
     */
    public CustomerResponse update(CustomerUpdateRequest request, UUID id) throws CustomerNotFoundException {
        CustomerEntity customer = this.customerRepository.findById(id)
                .orElseThrow(CustomerNotFoundException::new);

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

    /***
     * Delete customer soft delete
     * @param id UUID
     * @return true Boolean
     * @throws CustomerNotFoundException not found exception
     */
    public Boolean delete(UUID id) throws CustomerNotFoundException {
        CustomerEntity customer = this.customerRepository.findById(id)
                .orElseThrow(CustomerNotFoundException::new);

        customer.setDeleted(true);

        this.customerRepository.save(customer);

        return true;
    }
}
