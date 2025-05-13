package com.example.backend.domain.drone;
import com.example.backend.domain.drone.filtering.IDroneFilter;
import com.example.backend.domain.drone.mappers.DroneToRegisterMapper;
import com.example.backend.domain.drone.mappers.DroneEntityWithFlightRecordEntity;
import com.example.backend.domain.drone.requests.drones.DronesQuery;
import com.example.backend.domain.drone.requests.currentlyFlyingDrones.CurrentlyFlyingDronesQuery;
import com.example.backend.domain.drone.sorting.IDroneSort;
import com.example.backend.domain.flight.FlightRepository;
import com.example.backend.domain.flightRecord.FlightRecordRepository;
import com.example.backend.events.recordRegistration.model.DroneRecordToRegister;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DroneService {
    private final DroneRepository droneRepository;
    private final FlightRecordRepository flightRecordRepository;
    private final FlightRepository flightRepository;

    public DroneService(DroneRepository droneRepository, FlightRecordRepository flightRecordRepository, FlightRepository flightRepository) {
        this.droneRepository = droneRepository;
        this.flightRecordRepository = flightRecordRepository;
        this.flightRepository = flightRepository;
    }

    public List<DroneEntity> getDrones(List<IDroneFilter> filters, Optional<IDroneSort> droneSort){
        List<Specification<DroneEntity>> specifications = filters.stream()
                .map(IDroneFilter::toSpecification)
                .collect(Collectors.toList());

        droneSort.ifPresent(iDroneSort -> specifications.add(iDroneSort.toSpecification()));

        return new DronesQuery(droneRepository).execute(specifications);
    }

    public List<DroneEntity> getCurrentlyFlyingDrones(List<IDroneFilter> filters, Optional<IDroneSort> droneSort){
        List<Specification<DroneEntity>> specifications = filters.stream()
                .map(IDroneFilter::toSpecification)
                .collect(Collectors.toList());

        droneSort.ifPresent(iDroneSort -> specifications.add(iDroneSort.toSpecification()));

        var drones = new CurrentlyFlyingDronesQuery(droneRepository).execute(specifications);

        var dronesWithPosition = filterDronesWithoutRegisteredPosition(drones);

        return dronesWithLastThreeRecordsFromCurrentFlightIncluded(dronesWithPosition);
    }

    public Optional<DroneEntity> getDroneWithCurrentFlightTrace(String registration) {
        var foundDrone = droneRepository.findByRegistrationNumber(registration);

        if (foundDrone.isEmpty()){
            return Optional.empty();
        }

        var drone = foundDrone.get();
        var recordsFromCurrentFlight = drone.getFlightRecords()
                .stream().filter(r -> r.getFlight() == null)
                .sorted(new RecordTimestampsComparator())
                .toList();

        drone.setFlightRecords(recordsFromCurrentFlight);
        return Optional.of(drone);
    }

    public void upsertDronesRecords(List<DroneRecordToRegister> drones){
        var dronesToRegisterRegistrationNumbers = drones.stream().map(DroneRecordToRegister::getRegistrationNumber).toList();
        var curDrones = getAllByIds(dronesToRegisterRegistrationNumbers);

        var entitiesToSave = DroneToRegisterMapper.mapToEntities(drones, curDrones);


        var droneEntites = entitiesToSave
                .stream()
                .map(DroneEntityWithFlightRecordEntity::drone)
                .toList();
        var flightRecordEntites = entitiesToSave
                .stream()
                .map(DroneEntityWithFlightRecordEntity::flightRecord)
                .toList();

        this.flightRecordRepository.saveAll(flightRecordEntites);
        this.droneRepository
                .saveAll(droneEntites);

        for (var droneWithFlightRecordEntity : entitiesToSave){
            var droneEntity = droneWithFlightRecordEntity.drone();
            var flightRecordEntity = droneWithFlightRecordEntity.flightRecord();

            flightRecordEntity.setDrone(droneEntity);
        }

        this.flightRecordRepository.saveAll(flightRecordEntites);
    }

    private List<DroneEntity> getAllByIds(List<String> ids){
        return droneRepository.findAllById(ids);
    }

    private List<DroneEntity> filterDronesWithoutRegisteredPosition(List<DroneEntity> drones){
        return drones.stream().filter(drone -> drone.getFlightRecords().size() != 0).toList();
    }

    private List<DroneEntity> dronesWithLastThreeRecordsFromCurrentFlightIncluded(List<DroneEntity> drones){
        var result = new ArrayList<DroneEntity>();
        for (var drone : drones){
            var sortedAndFilteredRecords = drone.getFlightRecords().stream()
                    .filter(fr -> fr.getFlight() == null)
                    .sorted(new RecordTimestampsComparator()).toList();

            if (!sortedAndFilteredRecords.isEmpty()){
                var lastIndex = Math.min(3, sortedAndFilteredRecords.size());
                drone.setFlightRecords(sortedAndFilteredRecords.subList(0, lastIndex));
                result.add(drone);
            }
        }

        return result;
    }

    public List<DroneEntity> findAndStopDronesThatShouldStopFlying(List<String> registrationNumbers){
        List<DroneEntity> dronesThatShouldStopFlying;

        if(registrationNumbers.isEmpty()){
            dronesThatShouldStopFlying = droneRepository.findByIsAirborneTrue();
        }
        else {
            dronesThatShouldStopFlying = droneRepository.findByIsAirborneTrueAndRegistrationNumberNotIn(registrationNumbers); //this method doesn't work with empty list
        }

        stopDronesThatShouldStopFlying(dronesThatShouldStopFlying);

        return dronesThatShouldStopFlying;
    }

    private void stopDronesThatShouldStopFlying(List<DroneEntity> droneEntities){
        droneEntities.forEach(droneEntity -> {
            droneEntity.setAirborne(false);
            droneEntity.setType("Grounded");
        });

        droneRepository.saveAll(droneEntities);
    }
}
