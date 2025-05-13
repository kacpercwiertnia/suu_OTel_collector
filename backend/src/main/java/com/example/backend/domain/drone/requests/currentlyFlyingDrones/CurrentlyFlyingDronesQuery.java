package com.example.backend.domain.drone.requests.currentlyFlyingDrones;

import com.example.backend.domain.drone.DroneEntity;
import com.example.backend.domain.drone.DroneRepository;
import com.example.backend.common.filtering.infrastructure.SpecificationHelper;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class CurrentlyFlyingDronesQuery {
    private final DroneRepository repository;

    public CurrentlyFlyingDronesQuery(DroneRepository repository) {
        this.repository = repository;
    }

    public List<DroneEntity> execute(List<Specification<DroneEntity>> specifications){
        specifications.add(createDefaultConstraints());
        Specification<DroneEntity> combinedSpecifications = SpecificationHelper.combine(specifications);
        return repository.findAll(combinedSpecifications);
    }

    private Specification<DroneEntity> createDefaultConstraints(){
        return (Root<DroneEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> builder.isTrue(root.get("isAirborne"));
    }
}
