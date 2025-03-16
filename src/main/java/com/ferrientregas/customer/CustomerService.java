package com.ferrientregas.customer;

import com.ferrientregas.customer.dto.CustomerRequest;
import com.ferrientregas.customer.dto.CustomerResponse;
import com.ferrientregas.customer.dto.CustomerUpdateRequest;
import com.ferrientregas.customer.exception.CustomerNotFoundException;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

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
                        x.getFullName(),
                        x.getIdentification(),
                        x.getAddress(),
                        x.getAddressMaps(),
                        x.getPhone(),
                        x.getBirthDate()
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
                customer.getFullName(),
                customer.getIdentification(),
                customer.getAddress(),
                customer.getAddressMaps(),
                customer.getPhone(),
                customer.getBirthDate()
        );
    }

    /***
     * Create customer
     *
     * @param request customer
     * @return response
     */
    public CustomerResponse create(CustomerRequest request) {
        CustomerEntity customer = this.customerRepository.save(
                CustomerEntity.builder()
                        .fullName(request.fullname())
                        .identification(request.identification())
                        .address(request.address())
                        .addressMaps(request.addressMaps())
                        .phone(request.phone())
                        .birthDate(request.birthDate())
                        .build()
        );

        return new CustomerResponse(
                customer.getId(),
                customer.getFullName(),
                customer.getIdentification(),
                customer.getAddress(),
                customer.getAddressMaps(),
                customer.getPhone(),
                customer.getBirthDate()
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

        if (!StringUtils.isBlank(request.fullname())) {
            customer.setFullName(request.fullname());
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
                customer.getFullName(),
                customer.getIdentification(),
                customer.getAddress(),
                customer.getAddressMaps(),
                customer.getPhone(),
                customer.getBirthDate()
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
