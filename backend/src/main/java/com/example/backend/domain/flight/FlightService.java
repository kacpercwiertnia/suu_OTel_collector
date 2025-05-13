package com.example.backend.domain.flight;

import com.example.backend.domain.flight.filtering.IFlightFilter;
import com.example.backend.domain.flight.requests.flightSummaries.FlightsQuery;
import com.example.backend.domain.flight.sorting.IFlightSort;
import com.example.backend.domain.flightRecord.FlightRecordEntity;
import com.example.backend.domain.flightRecord.FlightRecordRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FlightService {
    private final FlightRepository flightRepository;
    private final FlightRecordRepository flightRecordRepository;

    public FlightService(FlightRepository flightRepository, FlightRecordRepository flightRecordRepository) {
        this.flightRepository = flightRepository;
        this.flightRecordRepository = flightRecordRepository;
    }

    public List<FlightEntity> getDroneFinishedFlights(String registrationNumber, List<IFlightFilter> filters, Optional<IFlightSort> flightSort){
        List<Specification<FlightEntity>> specifications = filters.stream()
                .map(IFlightFilter::toSpecification)
                .collect(Collectors.toList());

        flightSort.ifPresent(iFlightSort -> specifications.add(iFlightSort.toSpecification()));

        return new FlightsQuery(flightRepository).execute(registrationNumber, specifications);
    }

    public void createFlights(List<String> droneRegistrationNumbers, Boolean didLanded){
        List<FlightEntity> flightsToSave = new ArrayList<>();
        List<FlightRecordEntity> flightRecordsToUpdate = new ArrayList<>();

        for (var registrationNumber : droneRegistrationNumbers){
            var flight = new FlightEntity();
            var flightRecordsForFlight = getAllDroneRecordsFromCurrentFlight(registrationNumber);

            flight.summarizeFlight(flightRecordsForFlight, didLanded);
            flightRepository.save(flight);

            for (var flightRecordEntity : flightRecordsForFlight){
                flightRecordEntity.setFlight(flight);
            }

            flight.setFlightRecords(flightRecordsForFlight);

            flightsToSave.add(flight);
            flightRecordsToUpdate.addAll(flightRecordsForFlight);
        }

        flightRecordRepository.saveAll(flightRecordsToUpdate);
        flightRepository.saveAll(flightsToSave);
    }

    private List<FlightRecordEntity> getAllDroneRecordsFromCurrentFlight(String registrationNumber){
        var recordsFromCurrentFlight = flightRecordRepository
                .findAllByDrone_RegistrationNumberAndFlight_IdIsNull(registrationNumber);

        return recordsFromCurrentFlight;
    }

    public Optional<FlightEntity> getFlight(Long id){
        return flightRepository.findById(id);
    }
}
