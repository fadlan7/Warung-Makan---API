package com.enigma.wmb_api.specification;

import com.enigma.wmb_api.dto.request.SearchBillRequest;
import com.enigma.wmb_api.entity.Bill;
import com.enigma.wmb_api.util.DateUtil;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
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

            if (request.getMinTransDate() != null) {
                Date tempDate = DateUtil.parseDate(request.getMinTransDate(), "yyyy-MM-dd");
                Predicate birthDatePredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("transDate"), tempDate);
                predicates.add(birthDatePredicate);
            }

            if (request.getMaxTransDate() != null) {
                Date tempDate = DateUtil.parseDate(request.getMaxTransDate(), "yyyy-MM-dd");
                Predicate birthDatePredicate = criteriaBuilder.lessThanOrEqualTo(root.get("transDate"), tempDate);
                predicates.add(birthDatePredicate);
            }


            if (request.getTableId() != null) {
                Predicate tableIdPredicate = criteriaBuilder.like(criteriaBuilder.lower(
                        root.get("diningTable")), request.getTableId().toLowerCase()
                );
                predicates.add(tableIdPredicate);
            }

            if (request.getTransType() != null) {
                Predicate transTypePredicate = criteriaBuilder.like(criteriaBuilder.lower(
                        root.get("transactionType")), "%" + request.getTransType().toLowerCase() + "%"
                );
                predicates.add(transTypePredicate);
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}
