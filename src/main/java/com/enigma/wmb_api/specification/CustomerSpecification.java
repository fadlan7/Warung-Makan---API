package com.enigma.wmb_api.specification;

import com.enigma.wmb_api.dto.request.SearchCustomerRequest;
import com.enigma.wmb_api.entity.Customer;
import com.enigma.wmb_api.repository.CustomerRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CustomerSpecification {
    public static Specification<Customer> getSpecification(SearchCustomerRequest request) {
        CustomerRepository customerRepository = null;
        return (root, cq, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (request.getName() != null) {
                Predicate namePredicate = cb.like(cb.lower(root.get("name")), "%" + request.getName().toLowerCase() + "%");
                predicates.add(namePredicate);
            }

            if (request.getMobilePhoneNo() != null) {
                Predicate phonePredicate = cb.equal(root.get("mobilePhoneNo"), request.getMobilePhoneNo());
                predicates.add(phonePredicate);
            }

            if (request.getIsMember() != null) {
                Predicate statusPredicate = cb.equal(root.get("isMember"), request.getIsMember());
                predicates.add(statusPredicate);
            }

            return cq.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}