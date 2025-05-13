package com.example.backend.domain.drone;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DroneRepository extends JpaRepository<DroneEntity, String>, JpaSpecificationExecutor<DroneEntity> {
    Optional<DroneEntity> findByRegistrationNumber(String registrationNumber);
    List<DroneEntity> findByIsAirborneTrueAndRegistrationNumberNotIn(List<String> registrationNumbersList);
    List<DroneEntity> findByIsAirborneTrue();
}

