package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.request.SearchCustomerRequest;
import com.enigma.wmb_api.dto.request.UpdateCustomerRequest;
import com.enigma.wmb_api.dto.response.CustomerResponse;
import com.enigma.wmb_api.dto.response.GetCustomerResponse;
import com.enigma.wmb_api.entity.Customer;
import org.springframework.data.domain.Page;

public interface CustomerService {
    CustomerResponse create(Customer customer);

    Customer getById(String id);

    Customer getCustomerId(String id);

    Page<GetCustomerResponse> getAll(SearchCustomerRequest customer);

    CustomerResponse update(UpdateCustomerRequest customer);

    void delete(String id);

    void updateMemberStatusById(String id, Boolean isMember);
}
