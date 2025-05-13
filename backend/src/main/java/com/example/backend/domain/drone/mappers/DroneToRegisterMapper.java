package com.example.backend.domain.drone.mappers;

import com.example.backend.domain.drone.DroneEntity;
import com.example.backend.domain.flightRecord.FlightRecordEntity;
import com.example.backend.events.recordRegistration.model.DroneRecordToRegister;
import com.example.backend.events.recordRegistration.model.envelope.RegistrationFlag;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DroneToRegisterMapper {

    public static List<DroneEntityWithFlightRecordEntity> mapToEntities(List<DroneRecordToRegister> dronesToRegister, List<DroneEntity> alreadyRegisteredDrones){

        List<DroneEntityWithFlightRecordEntity> entites = new ArrayList<>();

        for(DroneRecordToRegister drone : dronesToRegister){
            entites.add(createOrUpdateDrone(drone, alreadyRegisteredDrones));
        }

        return entites;
    }

    private static DroneEntityWithFlightRecordEntity createOrUpdateDrone(DroneRecordToRegister drone, List<DroneEntity> curDrones){
        var searchedDrone = curDrones
                .stream()
                .filter(entity -> entity.getRegistrationNumber().equals(drone.getRegistrationNumber()))
                .findFirst();

        DroneEntity droneEntity = searchedDrone.orElse(DroneEntity.fromDroneToRegister(drone));

        var flightRecordEntity = FlightRecordEntity.fromFlightRecordToRegister(drone.getFlightRecord());

        droneEntity.getFlightRecords().add(flightRecordEntity);
        droneEntity.setAirborne(RegistrationFlag.mapToAirborne(drone.getFlightRecord().getFlag()));
        droneEntity.setType(RegistrationFlag.mapToType(drone.getFlightRecord().getFlag()));
        droneEntity.setRecentLatitude(flightRecordEntity.getLatitude());
        droneEntity.setRecentLongitude(flightRecordEntity.getLongitude());
        droneEntity.setRecentHeading(flightRecordEntity.getHeading());
        droneEntity.setRecentSpeed(flightRecordEntity.getSpeed());
        droneEntity.setRecentAltitude(flightRecordEntity.getAltitude());
        droneEntity.setRecentFuel(flightRecordEntity.getFuel());

        return new DroneEntityWithFlightRecordEntity(droneEntity, flightRecordEntity);
    }
}


