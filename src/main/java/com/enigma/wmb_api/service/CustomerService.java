package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.request.SearchCustomerRequest;
import com.enigma.wmb_api.dto.request.UpdateCustomerRequest;
import com.enigma.wmb_api.dto.response.CustomerResponse;
import com.enigma.wmb_api.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomerService {
    CustomerResponse create(Customer customer);

    Customer getById(String id);

    Page<Customer> getAll(SearchCustomerRequest customer);

    CustomerResponse update(UpdateCustomerRequest customer);

    void delete(String id);

    void updateMemberStatusById(String id, Boolean isMember);
}
