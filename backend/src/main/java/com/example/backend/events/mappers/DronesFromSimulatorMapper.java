package com.example.backend.events.mappers;

import com.example.backend.events.recordRegistration.model.DroneRecordToRegister;
import com.example.backend.simulatorIntegration.model.DroneFromSimulator;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DronesFromSimulatorMapper {
    public static List<DroneRecordToRegister> map(List<DroneFromSimulator> drones){
        List<DroneRecordToRegister> validRecords = new ArrayList<>();

        for(DroneFromSimulator drone : drones){
            try{
                validRecords.add(DroneRecordToRegister.fromDroneFromSimulator(drone));
            }catch(IllegalArgumentException ex){
                log.error("Could not register a record. Reason: " + ex);
            }
        }
        return validRecords;
    }
}
