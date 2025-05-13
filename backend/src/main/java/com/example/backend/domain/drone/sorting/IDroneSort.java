package com.example.backend.domain.drone.sorting;

import com.example.backend.domain.drone.DroneEntity;
import org.springframework.data.jpa.domain.Specification;

public interface IDroneSort {
    public Specification<DroneEntity> toSpecification();
}
