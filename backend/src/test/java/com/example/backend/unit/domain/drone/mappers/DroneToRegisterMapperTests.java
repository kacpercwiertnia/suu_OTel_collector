package com.example.backend.unit.domain.drone.mappers;

import com.example.backend.domain.drone.DroneEntity;
import com.example.backend.domain.drone.mappers.DroneEntityWithFlightRecordEntity;
import com.example.backend.domain.drone.mappers.DroneToRegisterMapper;
import com.example.backend.events.recordRegistration.model.DroneRecordToRegister;
import com.example.backend.simulatorIntegration.model.DroneFromSimulator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DroneToRegisterMapperTests {
    @Test
    public void ShouldCreateNewDroneEntity_DroneToRegisterIsNotPresentInCurrentDrones() {
        var droneToRegister = DroneRecordToRegister.fromDroneFromSimulator(
                new DroneFromSimulator(
                        "filename",
                        "server",
                        LocalDate.of(2012, 1, 1),
                        LocalTime.of(2, 2, 2, 2),
                        "UPD",
                        "id",
                        "idExt",
                        "500423N",
                        "1195723E",
                        90,
                        100,
                        50,
                        "Nigeria",
                        "operator",
                        7,
                        "Magenta",
                        "Belmondo",
                        "6969669",
                        "sign",
                        "Airborne",
                        69,
                        "signal",
                        "frequency",
                        "sensorLat",
                        "sensorLon",
                        "sensorLabel"
                )
        );

        List<DroneEntityWithFlightRecordEntity> result = DroneToRegisterMapper.mapToEntities(List.of(droneToRegister),
                new ArrayList<>());

        Assertions.assertEquals(result.size(), 1);

        var droneEntity = result.get(0).drone();

        Assertions.assertEquals(droneEntity.getRegistrationNumber(), "6969669");
        Assertions.assertTrue(droneEntity.isAirborne());
        Assertions.assertEquals(droneEntity.getCountry(), "Nigeria");
        Assertions.assertEquals(droneEntity.getOperator(), "operator");
        Assertions.assertEquals(droneEntity.getIdentification(), 7);
        Assertions.assertEquals(droneEntity.getIdentificationLabel(), "Magenta");
        Assertions.assertEquals(droneEntity.getModel(), "Belmondo");
        Assertions.assertEquals(droneEntity.getSign(), "sign");
        Assertions.assertEquals(droneEntity.getType(), "Airborne");

        var flightRecordEntity = result.get(0).flightRecord();

        Assertions.assertEquals(flightRecordEntity.getFilename(), "filename");
        Assertions.assertEquals(flightRecordEntity.getServer(), "server");
        Assertions.assertEquals(flightRecordEntity.getDate(), LocalDate.of(2012, 1, 1));
        Assertions.assertEquals(flightRecordEntity.getTime(), LocalTime.of(2, 2, 2, 2));
        Assertions.assertEquals(flightRecordEntity.getFlag(), "UPD");
        Assertions.assertEquals(flightRecordEntity.getSystemId(), "id");
        Assertions.assertEquals(flightRecordEntity.getLatitude(), 50.073055555555555);
        Assertions.assertEquals(flightRecordEntity.getLongitude(), 119.9563888888889);
        Assertions.assertEquals(flightRecordEntity.getHeading(), 90);
        Assertions.assertEquals(flightRecordEntity.getAltitude(), 50);
        Assertions.assertEquals(flightRecordEntity.getFuel(), 69);
    }

    @Test
    public void ShouldReturnEmptyList_WhenNoDronesToRegisterGiven() {
        var result = DroneToRegisterMapper.mapToEntities(new ArrayList<>(), new ArrayList<>());

        Assertions.assertEquals(result.size(), 0);
    }

    @Test
    public void ShouldUpdateExistingDrone_WhenDroneToRegisterPresentInCurrentDrones() {
        var droneToRegister = DroneRecordToRegister.fromDroneFromSimulator(
                new DroneFromSimulator(
                        "filename",
                        "server",
                        LocalDate.of(2012, 1, 1),
                        LocalTime.of(2, 2, 2, 2),
                        "UPD",
                        "id",
                        "idExt",
                        "500423N",
                        "1195723E",
                        90,
                        100,
                        50,
                        "Nigeria",
                        "operator",
                        7,
                        "Magenta",
                        "Belmondo",
                        "12345",
                        "sign",
                        "type",
                        69,
                        "signal",
                        "frequency",
                        "sensorLat",
                        "sensorLon",
                        "sensorLabel"
                )
        );

        var existingDrone = new DroneEntity("12345",
                true,
                "filipiny",
                "operatorAleInny",
                2,
                "czarny",
                "fajny",
                "znak",
                "Airborne",
                10,
                10,
                10,
                10,
                10,
                10);

        List<DroneEntityWithFlightRecordEntity> result = DroneToRegisterMapper.mapToEntities(List.of(droneToRegister),
                List.of(existingDrone));

        Assertions.assertEquals(result.size(), 1);

        var droneEntity = result.get(0).drone();

        Assertions.assertEquals(droneEntity.getRegistrationNumber(), "12345");
        Assertions.assertTrue(droneEntity.isAirborne());
        Assertions.assertEquals(droneEntity.getCountry(), "filipiny");
        Assertions.assertEquals(droneEntity.getOperator(), "operatorAleInny");
        Assertions.assertEquals(droneEntity.getIdentification(), 2);
        Assertions.assertEquals(droneEntity.getIdentificationLabel(), "czarny");
        Assertions.assertEquals(droneEntity.getModel(), "fajny");
        Assertions.assertEquals(droneEntity.getSign(), "znak");
        Assertions.assertEquals(droneEntity.getType(), "Airborne");

        var flightRecordEntity = result.get(0).flightRecord();

        Assertions.assertEquals(flightRecordEntity.getFilename(), "filename");
        Assertions.assertEquals(flightRecordEntity.getServer(), "server");
        Assertions.assertEquals(flightRecordEntity.getDate(), LocalDate.of(2012, 1, 1));
        Assertions.assertEquals(flightRecordEntity.getTime(), LocalTime.of(2, 2, 2, 2));
        Assertions.assertEquals(flightRecordEntity.getFlag(), "UPD");
        Assertions.assertEquals(flightRecordEntity.getSystemId(), "id");
        Assertions.assertEquals(flightRecordEntity.getLatitude(), 50.073055555555555);
        Assertions.assertEquals(flightRecordEntity.getLongitude(), 119.9563888888889);
        Assertions.assertEquals(flightRecordEntity.getHeading(), 90);
        Assertions.assertEquals(flightRecordEntity.getAltitude(), 50);
        Assertions.assertEquals(flightRecordEntity.getFuel(), 69);
    }
}
