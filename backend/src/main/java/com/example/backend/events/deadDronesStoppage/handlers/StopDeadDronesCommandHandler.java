package com.example.backend.events.deadDronesStoppage.handlers;

import com.example.backend.domain.drone.DroneEntity;
import com.example.backend.domain.drone.DroneService;
import com.example.backend.domain.flight.FlightService;
import com.example.backend.events.mediator.ICommandHandler;
import com.example.backend.events.deadDronesStoppage.commands.StopDeadDronesCommand;
import com.example.backend.events.mappers.DronesFromSimulatorMapper;
import com.example.backend.events.recordRegistration.model.DroneRecordToRegister;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class StopDeadDronesCommandHandler implements ICommandHandler<StopDeadDronesCommand> {
    private final DroneService droneService;
    private final FlightService flightService;

    public StopDeadDronesCommandHandler(DroneService droneService, FlightService flightService) {
        this.droneService = droneService;
        this.flightService = flightService;
    }

    @Transactional
    public void handle(StopDeadDronesCommand command) {
        var validRecords = DronesFromSimulatorMapper.map(command.drones());

        List<String> flyingDronesRegistrationNumbers = validRecords.stream().map(DroneRecordToRegister::getRegistrationNumber).toList();

        List<DroneEntity> dronesThatShouldStopFlying = droneService.findAndStopDronesThatShouldStopFlying(flyingDronesRegistrationNumbers);

        flightService.createFlights(dronesThatShouldStopFlying.stream().map(DroneEntity::getRegistrationNumber).toList(), false);
    }
}
