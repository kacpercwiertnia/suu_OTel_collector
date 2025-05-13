package com.example.backend.domain.flight.filtering;

import com.example.backend.domain.flight.FlightEntity;
import org.springframework.data.jpa.domain.Specification;

public interface IFlightFilter {
    public Specification<FlightEntity> toSpecification();
}
