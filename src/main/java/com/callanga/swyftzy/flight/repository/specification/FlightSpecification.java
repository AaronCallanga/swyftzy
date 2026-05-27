package com.callanga.swyftzy.flight.repository.specification;

import com.callanga.swyftzy.flight.entity.Flight;
import com.callanga.swyftzy.flight.repository.filter.FlightFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class FlightSpecification {

    public static Specification<Flight> withFilters(FlightFilter filter) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter.getOrigin() != null) {
                predicates.add(criteriaBuilder.equal(root.get("origin"), filter.getOrigin()));
            }

            if (filter.getDestinations() != null && !filter.getDestinations().isEmpty()) {
                predicates.add(root.get("destination").in(filter.getDestinations()));
            }

            if (filter.getStartOfDay() != null && filter.getEndOfDay() != null) {
                predicates.add(criteriaBuilder.between(
                        root.get("departureTime"),
                        filter.getStartOfDay(),
                        filter.getEndOfDay()
                                         ));
            }

            if (filter.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), filter.getStatus()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
