package com.example.backend.domain.drone.requests.drones;

import com.example.backend.domain.drone.DroneEntity;
import com.example.backend.domain.drone.DroneRepository;
import com.example.backend.common.filtering.infrastructure.SpecificationHelper;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class DronesQuery {
    private final DroneRepository repository;

    public DronesQuery(DroneRepository repository) {
        this.repository = repository;
    }

    public List<DroneEntity> execute(List<Specification<DroneEntity>> specifications){
        Specification<DroneEntity> combinedSpecifications = SpecificationHelper.combine(specifications);
        return repository.findAll(combinedSpecifications);
    }
}
