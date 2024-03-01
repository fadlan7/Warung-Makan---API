package com.enigma.wmb_api.specification;

import com.enigma.wmb_api.dto.request.SearchMenuRequest;
import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.repository.MenuRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MenuSpecification {
    public static Specification<Menu> getSpecification(SearchMenuRequest request) {
        MenuRepository customerRepository = null;
        return (root, cq, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (request.getName() != null) {
                Predicate namePredicate = cb.like(cb.lower(root.get("name")), "%" + request.getName().toLowerCase() + "%");
                predicates.add(namePredicate);
            }

            if (request.getPrice() != null) {
                Predicate pricePredicate = cb.equal(root.get("price"), request.getPrice());
                predicates.add(pricePredicate);
            }

            if (request.getMinPrice() != null) {
                Predicate pricePredicate = cb.greaterThanOrEqualTo(root.get("price"), request.getMinPrice());
                predicates.add(pricePredicate);
            }

            if (request.getMaxPrice() != null) {
                Predicate pricePredicate = cb.lessThanOrEqualTo(root.get("price"), request.getMaxPrice());
                predicates.add(pricePredicate);
            }

            return cq.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}
