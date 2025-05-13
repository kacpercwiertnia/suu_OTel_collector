package com.example.backend.integration.drone;

import com.example.backend.ValidatedDroneRecordsCounter;
import com.example.backend.domain.drone.DroneRepository;
import com.example.backend.domain.drone.DroneService;
import com.example.backend.domain.flight.FlightRepository;
import com.example.backend.domain.flight.FlightService;
import com.example.backend.domain.flightRecord.FlightRecordEntity;
import com.example.backend.domain.flightRecord.FlightRecordRepository;
import com.example.backend.domain.flightRecord.FlightRecordService;
import com.example.backend.events.mediator.ICommandHandler;
import com.example.backend.events.mediator.Mediator;
import com.example.backend.events.deadDronesStoppage.commands.StopDeadDronesCommand;
import com.example.backend.events.recordRegistration.commands.SaveRecordsCommand;
import com.example.backend.events.deadDronesStoppage.handlers.StopDeadDronesCommandHandler;
import com.example.backend.events.recordRegistration.handlers.SaveRecordsCommandHandler;
import com.example.backend.simulatorIntegration.model.DroneFromSimulator;
import com.example.backend.unit.domain.drone.DroneEntityFixtureBuilder;
import com.example.backend.unit.domain.flightRecord.FlightRecordEntityFixtureBuilder;
import com.example.backend.unit.simulatorIntegration.model.DroneFromSimulatorFixtureBuilder;
import io.opentelemetry.api.OpenTelemetry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class SimulatorIntegrationTests {
    @Autowired
    private TestEntityManager fakeDb;
    @Autowired
    private DroneRepository droneRepository;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private FlightRecordRepository flightRecordRepository;
    private DroneService droneService;
    private FlightService flightService;
    private Mediator sut;
    private DroneFromSimulatorFixtureBuilder builder;

    @BeforeEach
    public void setUp(){
        droneService = new DroneService(droneRepository, flightRecordRepository, flightRepository);
        flightService = new FlightService(flightRepository, flightRecordRepository);
        FlightRecordService flightRecordService = new FlightRecordService(flightRecordRepository);
        FlightService flightService = new FlightService(flightRepository, flightRecordRepository);
        ICommandHandler<SaveRecordsCommand> saveHandler = new SaveRecordsCommandHandler(flightRecordService, droneService, flightService, Mockito.mock(ValidatedDroneRecordsCounter.class));
        ICommandHandler<StopDeadDronesCommand> stopHandler = new StopDeadDronesCommandHandler(droneService, flightService);
        builder = new DroneFromSimulatorFixtureBuilder();
        sut = new Mediator(saveHandler, stopHandler);
    }

    @Test
    public void ShouldCreateDrone() {
        var drones = List.of(builder.withFlag("BEG").build());

        SaveRecordsCommand saveCommand = new SaveRecordsCommand(drones);
        StopDeadDronesCommand checkCommand = new StopDeadDronesCommand(drones);

        sut.send(saveCommand);
        sut.send(checkCommand);

        var droneResult = droneService.getDrones(new ArrayList<>(), Optional.empty());

        Assertions.assertEquals(1, droneResult.size());
        Assertions.assertEquals("420-69-2137", droneResult.get(0).getRegistrationNumber());
        Assertions.assertTrue(droneResult.get(0).isAirborne());
        Assertions.assertEquals("Airborne", droneResult.get(0).getType());

        var droneFlight = flightService.getDroneFinishedFlights(droneResult.get(0).getRegistrationNumber(), new ArrayList<>() {
        }, Optional.empty());
        Assertions.assertEquals(0, droneFlight.size());
    }

    @Test
    public void ShouldStopDrone_AndFinishFlight_WhenSimulatorReturnedNoRecords() {
        setupDatabase();
        var drones = new ArrayList<DroneFromSimulator>();

        SaveRecordsCommand saveCommand = new SaveRecordsCommand(drones);
        StopDeadDronesCommand checkCommand = new StopDeadDronesCommand(drones);

        sut.send(saveCommand);
        sut.send(checkCommand);

        var droneResult = droneService.getDrones(new ArrayList<>(), Optional.empty());

        Assertions.assertEquals(1, droneResult.size());
        Assertions.assertEquals("flyingDroneWithRecords", droneResult.get(0).getRegistrationNumber());
        Assertions.assertFalse(droneResult.get(0).isAirborne());
        Assertions.assertEquals("Grounded", droneResult.get(0).getType());

        var droneFlight = flightService.getDroneFinishedFlights(droneResult.get(0).getRegistrationNumber(), new ArrayList<>(),Optional.empty());
        Assertions.assertEquals(1, droneFlight.size());
        Assertions.assertFalse(droneFlight.get(0).isDidLand());
    }

    @Test
    public void ShouldStopDrone_AndFinishFlight_WhenSimulatorReturnedRecords() {
        setupDatabase();
        var drones = List.of(builder.withFlag("BEG").withFilename("filname2").build());

        SaveRecordsCommand saveCommand = new SaveRecordsCommand(drones);
        StopDeadDronesCommand checkCommand = new StopDeadDronesCommand(drones);

        sut.send(saveCommand);
        sut.send(checkCommand);

        var droneResult = droneService.getDrones(new ArrayList<>(), Optional.empty());

        Assertions.assertEquals(2, droneResult.size());
        Assertions.assertEquals("flyingDroneWithRecords", droneResult.get(0).getRegistrationNumber());
        Assertions.assertFalse(droneResult.get(0).isAirborne());
        Assertions.assertEquals("Grounded", droneResult.get(0).getType());
        Assertions.assertEquals("420-69-2137", droneResult.get(1).getRegistrationNumber());
        Assertions.assertTrue(droneResult.get(1).isAirborne());
        Assertions.assertEquals("Airborne", droneResult.get(1).getType());

        var droneFlight = flightService.getDroneFinishedFlights(droneResult.get(0).getRegistrationNumber(), new ArrayList<>(),Optional.empty());
        Assertions.assertEquals(1, droneFlight.size());
        Assertions.assertFalse(droneFlight.get(0).isDidLand());
    }

    @Test
    public void ShouldAddFlightRecord_WhenSimulatorReturnedRecords() {
        setupDatabase();
        var drones = List.of(builder.withFlag("UPD").withFilename("filname2").withRegistrationNumber("flyingDroneWithRecords").build());

        SaveRecordsCommand saveCommand = new SaveRecordsCommand(drones);
        StopDeadDronesCommand checkCommand = new StopDeadDronesCommand(drones);

        sut.send(saveCommand);
        sut.send(checkCommand);

        var droneResult = droneService.getDrones(new ArrayList<>(), Optional.empty());

        Assertions.assertEquals(1, droneResult.size());
        Assertions.assertEquals("flyingDroneWithRecords", droneResult.get(0).getRegistrationNumber());
        Assertions.assertTrue(droneResult.get(0).isAirborne());
        Assertions.assertEquals("Airborne", droneResult.get(0).getType());
        Assertions.assertEquals(2, droneResult.get(0).getFlightRecords().size());

        var droneFlight = flightService.getDroneFinishedFlights(droneResult.get(0).getRegistrationNumber(), new ArrayList<>(), Optional.empty());
        Assertions.assertEquals(0, droneFlight.size());
    }

    private void setupDatabase(){
        persistDroneWithFlightRecord("1", "flyingDroneWithRecords", true);
    }

    private void persistDroneWithFlightRecord(String flightRecordId, String droneRegNumber, boolean isFlying){
        var flightRecord = new FlightRecordEntityFixtureBuilder()
                .withId(flightRecordId)
                .withDateAndTime(LocalDate.now(), LocalTime.now())
                .build();
        fakeDb.persistAndFlush(flightRecord);

        var flightRecords = new ArrayList<FlightRecordEntity>();
        flightRecords.add(flightRecord);

        var drone = new DroneEntityFixtureBuilder()
                .withRegistrationNumber(droneRegNumber)
                .withFlyingRecords(flightRecords)
                .withIsAirbourne(isFlying)
                .build();
        fakeDb.persistAndFlush(drone);

        flightRecord.setDrone(drone);
        fakeDb.persistAndFlush(flightRecord);
    }
}
