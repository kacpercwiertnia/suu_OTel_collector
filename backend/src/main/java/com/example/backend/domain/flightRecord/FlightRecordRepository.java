package com.example.backend.domain.flightRecord;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRecordRepository extends JpaRepository<FlightRecordEntity, String> {
    public Optional<FlightRecordEntity> findFirstByFilename(String filename);

    public List<FlightRecordEntity> findAllByDrone_RegistrationNumberAndFlight_IdIsNull(String registrationNumber);
}
