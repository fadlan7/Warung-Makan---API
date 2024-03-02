package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.request.SearchCustomerRequest;
import com.enigma.wmb_api.entity.Customer;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerService {
    Customer create(Customer customer);

    Customer getById(String id);

    Page<Customer> getAll(SearchCustomerRequest customer);

    Customer update(Customer customer);

    void delete(String id);

    void updateMemberStatusById(String id, Boolean isMember);
}
