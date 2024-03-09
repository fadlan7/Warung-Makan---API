package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.dto.request.SearchCustomerRequest;
import com.enigma.wmb_api.dto.request.UpdateCustomerRequest;
import com.enigma.wmb_api.dto.response.CustomerResponse;
import com.enigma.wmb_api.dto.response.GetCustomerResponse;
import com.enigma.wmb_api.dto.response.UserAccountResponse;
import com.enigma.wmb_api.entity.Customer;
import com.enigma.wmb_api.entity.UserAccount;
import com.enigma.wmb_api.repository.CustomerRepository;
import com.enigma.wmb_api.service.CustomerService;
import com.enigma.wmb_api.service.UserService;
import com.enigma.wmb_api.specification.CustomerSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserService userService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CustomerResponse create(Customer customer) {
        Customer newCustomer = customerRepository.saveAndFlush(customer);

        return convertCustomerToCustomerResponse(newCustomer);
    }

    @Transactional(readOnly = true)
    @Override
    public Customer getById(String id) {
        return findByIdOrThrowNotFound(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<GetCustomerResponse> getAll(SearchCustomerRequest request) {
        if (request.getPage() <= 0) request.setPage(1);

        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getSize(), sort);

        Specification<Customer> specification = CustomerSpecification.getSpecification(request);
//        return customerRepository.findAll(specification, pageable);
        Page<Customer> customers = customerRepository.findAll(specification, pageable);

        List<GetCustomerResponse> customerResponses = customers.getContent().stream().map(account -> {
            UserAccountResponse userAccountResponses =
                    Optional.ofNullable(account.getUserAccount())
                            .map(userAccount -> UserAccountResponse.builder()
                                    .username(userAccount.getUsername())
                                    .isEnable(userAccount.getIsEnable())
                                    .roles(userAccount.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                                    .build())
                            .orElse(null);

            return GetCustomerResponse.builder()
                    .id(account.getId())
                    .name(account.getName())
                    .mobilePhoneNo(account.getMobilePhoneNo())
                    .isMember(account.getIsMember())
                    .userAccount(userAccountResponses)
                    .build();
        }).toList();
        return new PageImpl<>(customerResponses, pageable, customers.getTotalElements());
    }

    @Transactional(rollbackFor = Exception.class)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN') OR @authenticateUserServiceImpl.hasSameId(#request)")
    @Override
    public CustomerResponse update(UpdateCustomerRequest request) {
        Customer currentCustomer = findByIdOrThrowNotFound(request.getId());

        currentCustomer.setName(request.getName());
        currentCustomer.setMobilePhoneNo(request.getMobilePhoneNo());
        customerRepository.saveAndFlush(currentCustomer);

        return convertCustomerToCustomerResponse(currentCustomer);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        Customer customer = findByIdOrThrowNotFound(id);
        customerRepository.delete(customer);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateMemberStatusById(String id, Boolean isMember) {
        findByIdOrThrowNotFound(id);
        customerRepository.updateMember(id, isMember);
    }

    public Customer findByIdOrThrowNotFound(String id) {
        return customerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
    }

    private CustomerResponse convertCustomerToCustomerResponse(Customer newCustomer) {
        return CustomerResponse.builder()
                .id(newCustomer.getId())
                .name(newCustomer.getName())
                .mobilePhoneNo(newCustomer.getMobilePhoneNo())
                .userAccountId(newCustomer.getUserAccount().getId())
                .build();
    }
}
