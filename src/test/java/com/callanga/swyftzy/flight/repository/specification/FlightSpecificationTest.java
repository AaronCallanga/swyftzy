package com.callanga.swyftzy.flight.repository.specification;

import com.callanga.swyftzy.flight.entity.Flight;
import com.callanga.swyftzy.flight.enums.FlightStatus;
import com.callanga.swyftzy.flight.repository.filter.FlightFilter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightSpecificationTest {

    @Mock
    private Root<Flight> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Test
    void withFilters_whenFilterIsNull_returnsConjunction() {
        Predicate conjunction = mock(Predicate.class);
        when(criteriaBuilder.conjunction()).thenReturn(conjunction);

        Specification<Flight> spec = FlightSpecification.withFilters(null);
        Predicate result = spec.toPredicate(root, query, criteriaBuilder);

        assertThat(result).isEqualTo(conjunction);
        verify(criteriaBuilder).conjunction();
        verifyNoMoreInteractions(criteriaBuilder);
    }

    @Test
    void withFilters_whenDestinationsEmpty_doesNotAddDestinationPredicate() {
        FlightFilter filter = FlightFilter.builder()
                .destinations(Collections.emptyList())
                .build();

        Specification<Flight> spec = FlightSpecification.withFilters(filter);
        spec.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder, never()).lower(any());
    }
}
