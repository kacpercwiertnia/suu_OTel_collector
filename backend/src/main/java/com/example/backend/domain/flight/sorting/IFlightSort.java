package com.example.backend.domain.flight.sorting;

import com.example.backend.domain.flight.FlightEntity;
import org.springframework.data.jpa.domain.Specification;

public interface IFlightSort {
    public Specification<FlightEntity> toSpecification();
}
