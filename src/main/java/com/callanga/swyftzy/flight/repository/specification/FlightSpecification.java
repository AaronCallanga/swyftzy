package com.callanga.swyftzy.flight.repository.specification;

import com.callanga.swyftzy.flight.entity.Flight;
import com.callanga.swyftzy.flight.repository.filter.FlightFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FlightSpecification {

    public static Specification<Flight> withFilters(FlightFilter filter) {
        if (filter == null) {
            return (root, query, cb) -> cb.conjunction();
        }

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter.getOrigin() != null) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("origin")),
                        filter.getOrigin().toLowerCase()
                                                    ));
            }

            if (filter.getDestinations() != null && !filter.getDestinations().isEmpty()) {
                predicates.add(
                        criteriaBuilder.lower(root.get("destination"))
                                       .in(filter.getDestinations()
                                                 .stream()
                                                 .map(String::toLowerCase)
                                                 .toList()
                                          )
                              );
            }

            if (filter.getDepartureDate() != null) {
                LocalDateTime start = filter.getDepartureDate().atStartOfDay();
                LocalDateTime end = filter.getDepartureDate().atTime(LocalTime.MAX);

                predicates.add(criteriaBuilder.between(
                        root.get("departureTime"),
                        start,
                        end
                                         ));
            }

            if (filter.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), filter.getStatus()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
