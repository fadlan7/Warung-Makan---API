package com.enigma.wmb_api.specification;

import com.enigma.wmb_api.entity.DiningTable;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DiningTableSpecification {
    public static Specification<DiningTable> getSpecification(String q) {
        return (root, cq, cb) -> {
            if (!StringUtils.hasText(q)) return cb.conjunction();

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + q.toLowerCase() + "%"));

            return cb.or(predicates.toArray(new Predicate[]{}));
        };
    }
}
