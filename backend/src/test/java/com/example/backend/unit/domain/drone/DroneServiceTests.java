package com.example.backend.unit.domain.drone;

import com.example.backend.domain.drone.DroneEntity;
import com.example.backend.domain.drone.DroneRepository;
import com.example.backend.domain.drone.DroneService;
import com.example.backend.domain.flight.FlightEntity;
import com.example.backend.domain.flight.FlightRepository;
import com.example.backend.unit.domain.flightRecord.FlightRecordEntityFixtureBuilder;
import com.example.backend.domain.flightRecord.FlightRecordRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DroneServiceTests {
    private DroneService sut;
    private DroneRepository droneRepository;
    private FlightRecordRepository flightRecordRepository;
    private FlightRepository flightRepository;

    @BeforeEach
    public void setUp()
    {
        droneRepository = Mockito.mock(DroneRepository.class);
        flightRecordRepository = Mockito.mock(FlightRecordRepository.class);
        flightRepository = Mockito.mock(FlightRepository.class);
        sut = new DroneService(droneRepository, flightRecordRepository, flightRepository);
    }

    @Test
    public void ShouldReturnAllCurrentlyFlyingDrones_WithOnlyThreeLastPositions(){
        //prepare
        Mockito.when(droneRepository.findAll(Mockito.<Specification<DroneEntity>>any()))
                .thenReturn(
                List.of(getCurrentlyFlyingDroneEntityWithMultipleFlightRecords())
        );

        //act
        var result = sut.getCurrentlyFlyingDrones(new ArrayList<>(), Optional.empty());

        //assert

        Assertions.assertEquals(result.size(), 1);
        var flights = result.get(0).getFlightRecords();

        Assertions.assertEquals(flights.size(), 3);

        Assertions.assertEquals(flights.get(0).getDate(), LocalDate.of(2012, 7, 3));
        Assertions.assertEquals(flights.get(1).getDate(), LocalDate.of(2012, 5, 3));
        Assertions.assertEquals(flights.get(2).getDate(), LocalDate.of(2012, 3, 3));
    }

    @Test
    public void ShouldReturnAllCurrentlyFlyingDrones_ThatHaveAtLeastOneRegisteredPosition(){
        //prepare

        var droneWithRecords = getCurrentlyFlyingDroneEntityWithMultipleFlightRecords();
        var droneWithoutRecords = new DroneEntityFixtureBuilder()
                .withRegistrationNumber("megaDroniarz")
                .withIsAirbourne(true)
                .build();

        Mockito.when(droneRepository.findAll(Mockito.<Specification<DroneEntity>>any())).thenReturn(
                List.of(droneWithRecords, droneWithoutRecords)
        );

        //act
        var result = sut.getCurrentlyFlyingDrones(new ArrayList<>(), Optional.empty());

        //assert
        Assertions.assertEquals(result.size(), 1);

        var drone = result.get(0);

        Assertions.assertEquals(drone, droneWithRecords);
    }

    @Test
    public void ShouldReturnEmptyList_IfNoRegisteredDrones(){
        Mockito.when(droneRepository.findAll(Mockito.<Specification<DroneEntity>>any())).thenReturn(
                new ArrayList<>()
        );

        var result = sut.getCurrentlyFlyingDrones(new ArrayList<>(), Optional.empty());

        Assertions.assertEquals(result.size(), 0);
    }

    @Test
    public void ShouldReturnDrone_WithAllFlightsRecordFromCurrentFlight(){
        //prepare
        var drone = getCurrentlyFlyingDroneEntityWithMultipleFlightRecords();
        Mockito.when(droneRepository.findByRegistrationNumber(Mockito.any()))
                .thenReturn(Optional.of(drone));

        //act
        var result = sut.getDroneWithCurrentFlightTrace("krokodyl");

        //assert
        Assertions.assertTrue(result.isPresent());

        var droneWithFlightRecords = result.get();

        Assertions.assertEquals(droneWithFlightRecords.getFlightRecords().size(), 5);
    }

    @Test
    public void ShouldNotReturnFlightRecords_FromPreviousFlights(){
        //prepare
        var flightRecord = new FlightRecordEntityFixtureBuilder()
                .withId("10")
                .withFlight(new FlightEntity())
                .build();

        var drone = new DroneEntityFixtureBuilder()
                .withRegistrationNumber("megaDroniarz")
                .withIsAirbourne(true)
                .withFlyingRecords(List.of(flightRecord))
                .build();
        Mockito.when(droneRepository.findByRegistrationNumber(Mockito.any()))
                .thenReturn(Optional.of(drone));

        //act
        var result = sut.getDroneWithCurrentFlightTrace("krokodyl");

        //assert
        Assertions.assertTrue(result.isPresent());
        var droneEntity = result.get();

        Assertions.assertEquals(droneEntity.getFlightRecords().size(), 0);
    }

    @Test
    public void ShouldReturnEmptyOptional_IfDroneNotFound(){
        Mockito.when(droneRepository.findByRegistrationNumber(Mockito.any()))
                .thenReturn(Optional.empty());

        var result = sut.getDroneWithCurrentFlightTrace("krokodylisko");

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnAllDrones_WithAllPositions(){
        Mockito.when(droneRepository.findAll(Mockito.<Specification<DroneEntity>>any()))
                .thenReturn(
                        List.of(getCurrentlyFlyingDroneEntityWithMultipleFlightRecords(), getDroneEntityWithMultipleFlightRecords())
                );

        var result = sut.getDrones(new ArrayList<>(), Optional.empty());

        Assertions.assertEquals(result.size(), 2);

        var flights1 = result.get(0).getFlightRecords();
        var flights2 = result.get(1).getFlightRecords();

        Assertions.assertEquals(flights1.size(), 5);
        Assertions.assertEquals(flights2.size(),5);
    }

    private DroneEntity getDroneEntityWithMultipleFlightRecords(){
        var flightRecords = List.of(
                new FlightRecordEntityFixtureBuilder()
                        .withId("1")
                        .withDateAndTime(LocalDate.of(2012, 3, 3), LocalTime.of(3, 3, 3, 3))
                        .build(),
                new FlightRecordEntityFixtureBuilder()
                        .withId("2")
                        .withDateAndTime(LocalDate.of(2012, 2, 3),LocalTime.of(3, 3, 3, 3))
                        .build(),
                new FlightRecordEntityFixtureBuilder()
                        .withId("3")
                        .withDateAndTime(LocalDate.of(2012, 7, 3), LocalTime.of(3, 3, 3, 3))
                        .build(),
                new FlightRecordEntityFixtureBuilder()
                        .withId("4")
                        .withDateAndTime(LocalDate.of(2012, 1, 3), LocalTime.of(3, 3, 3, 3))
                        .build(),
                new FlightRecordEntityFixtureBuilder()
                        .withId("5")
                        .withDateAndTime(LocalDate.of(2012, 5, 3), LocalTime.of(3, 3, 3, 3))
                        .build()
        );

        var droneEntity = new DroneEntity(
                "gigaDron",
                false,
                "Belarus",
                "PL",
                4,
                "yellow",
                "najlepszy",
                "yes",
                "Grounded",
                10,
                10,
                10,
                10,
                10,
                10
        );
        droneEntity.setFlightRecords(flightRecords);

        return droneEntity;
    }

    private DroneEntity getCurrentlyFlyingDroneEntityWithMultipleFlightRecords(){
        var flightRecords = List.of(
                new FlightRecordEntityFixtureBuilder()
                        .withId("1")
                        .withDateAndTime(LocalDate.of(2012, 3, 3), LocalTime.of(3, 3, 3, 3))
                        .build(),
                new FlightRecordEntityFixtureBuilder()
                        .withId("2")
                        .withDateAndTime(LocalDate.of(2012, 2, 3),LocalTime.of(3, 3, 3, 3))
                        .build(),
                new FlightRecordEntityFixtureBuilder()
                        .withId("3")
                        .withDateAndTime(LocalDate.of(2012, 7, 3), LocalTime.of(3, 3, 3, 3))
                        .build(),
                new FlightRecordEntityFixtureBuilder()
                        .withId("4")
                        .withDateAndTime(LocalDate.of(2012, 1, 3), LocalTime.of(3, 3, 3, 3))
                        .build(),
                new FlightRecordEntityFixtureBuilder()
                        .withId("5")
                        .withDateAndTime(LocalDate.of(2012, 5, 3), LocalTime.of(3, 3, 3, 3))
                        .build()
        );

        var droneEntity = new DroneEntity(
                "megaDroniarz",
                true,
                "Hawana",
                "Michael Jackson",
                3,
                "pink",
                "szybcior",
                "znak",
                "Airborne",
                10,
                10,
                10,
                10,
                10,
                10
        );
        droneEntity.setFlightRecords(flightRecords);

        return droneEntity;
    }
}
