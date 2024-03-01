package com.enigma.wmb_api.specification;

import com.enigma.wmb_api.dto.request.SearchDiningTableRequest;
import com.enigma.wmb_api.entity.DiningTable;
import com.enigma.wmb_api.repository.DiningTableRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class DiningTableSpecification {
    public static Specification<DiningTable> getSpecification(SearchDiningTableRequest request) {
        DiningTableRepository customerRepository = null;
        return (root, cq, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (request.getName() != null) {
                Predicate namePredicate = cb.like(cb.lower(root.get("name")), "%" + request.getName().toLowerCase() + "%");
                predicates.add(namePredicate);
            }

            return cq.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}
