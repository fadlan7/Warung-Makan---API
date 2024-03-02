package com.enigma.wmb_api.specification;

import com.enigma.wmb_api.dto.request.SearchBillRequest;
import com.enigma.wmb_api.entity.Bill;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BillSpecification {
    public static Specification<Bill> getSpecification(SearchBillRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getId() != null) {
                Predicate billIdPredicate = criteriaBuilder.like(criteriaBuilder.lower(
                        root.get("id")), request.getId().toLowerCase()
                );
                predicates.add(billIdPredicate);
            }

            if (request.getCustomerId() != null) {
                Predicate customerIdPredicate = criteriaBuilder.like(criteriaBuilder.lower(
                        root.get("customer_id")), request.getCustomerId().toLowerCase()
                );
                predicates.add(customerIdPredicate);
            }

            if (request.getTableId() != null) {
                Predicate tableIdPredicate = criteriaBuilder.like(criteriaBuilder.lower(
                        root.get("table_id")), request.getTableId().toLowerCase()
                );
                predicates.add(tableIdPredicate);
            }

            if (request.getTransType() != null) {
                Predicate transTypePredicate = criteriaBuilder.like(criteriaBuilder.lower(
                        root.get("trans_type")), "%" + request.getTransType().toLowerCase() + "%"
                );
                predicates.add(transTypePredicate);
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}
