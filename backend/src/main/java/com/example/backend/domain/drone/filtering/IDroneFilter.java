package com.example.backend.domain.drone.filtering;

import com.example.backend.domain.drone.DroneEntity;
import org.springframework.data.jpa.domain.Specification;

public interface IDroneFilter {
    public Specification<DroneEntity> toSpecification();
}
