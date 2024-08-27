package com.enigma.wmb_api.specification;

import com.enigma.wmb_api.dto.request.SearchMenuRequest;
import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.repository.MenuRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MenuSpecification {
    public static Specification<Menu> getSpecification(String q) {
        return (root, cq, cb) -> {
            if (!StringUtils.hasText(q)) return cb.conjunction();

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + q.toLowerCase() + "%"));

            try {
                Long price = Long.valueOf(q);
                predicates.add(cb.equal(root.get("price"), price));
            } catch (NumberFormatException e) {
                // Ignore
            }

            return cb.or(predicates.toArray(new Predicate[]{}));
        };
    }
}
