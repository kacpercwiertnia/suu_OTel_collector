package com.example.backend.unit.events.mappers;

import com.example.backend.events.mappers.DronesFromSimulatorMapper;
import com.example.backend.events.recordRegistration.model.envelope.Heading;
import com.example.backend.events.recordRegistration.model.envelope.Identification;
import com.example.backend.events.recordRegistration.model.envelope.Latitude;
import com.example.backend.events.recordRegistration.model.envelope.Longitude;
import com.example.backend.events.recordRegistration.model.envelope.RegistrationFlag;
import com.example.backend.simulatorIntegration.model.DroneFromSimulator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DronesFromSimulatorMapperTests {
    @Test
    public void shouldReturnMapDroneRecords() {
        var date = LocalDate.now();
        var time = LocalTime.now();

        var drones = List.of(
                new DroneFromSimulator(
                        "filename",
                        "server",
                        date,
                        time,
                        "DROP",
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
                        "420-69-2137",
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

        var result = DronesFromSimulatorMapper.map(drones);

        Assertions.assertEquals(result.size(), 1);

        var droneRecord = result.get(0);

        Assertions.assertEquals(droneRecord.getCountry(), "Nigeria");
        Assertions.assertEquals(droneRecord.getOperator(), "operator");
        Assertions.assertEquals(droneRecord.getModel(), "Belmondo");
        Assertions.assertEquals(droneRecord.getSign(), "sign");
        Assertions.assertEquals(droneRecord.getIdentification(), new Identification(7));
        Assertions.assertEquals(droneRecord.getType(), "type");

        var flightRecord = droneRecord.getFlightRecord();

        Assertions.assertEquals(flightRecord.getId(), "idExt");
        Assertions.assertEquals(flightRecord.getAltitude(), 50);
        Assertions.assertEquals(flightRecord.getFuel(), 69);
        Assertions.assertEquals(flightRecord.getFlag(), RegistrationFlag.DROP);
        Assertions.assertEquals(flightRecord.getDate(), date);
        Assertions.assertEquals(flightRecord.getHeading(), new Heading(90));
        Assertions.assertEquals(flightRecord.getFilename(), "filename");
        Assertions.assertEquals(flightRecord.getLatitude(), new Latitude("500423N"));
        Assertions.assertEquals(flightRecord.getLongitude(), new Longitude("1195723E"));
        Assertions.assertEquals(flightRecord.getServer(), "server");
        Assertions.assertEquals(flightRecord.getSpeed(), 100);
        Assertions.assertEquals(flightRecord.getTime(), time);
        Assertions.assertEquals(flightRecord.getSystemId(), "id");
    }

    @Test
    public void ShouldNotReturnMappedDroneRecords_IfDataIsIncorrect(){
        var date = LocalDate.now();
        var time = LocalTime.now();

        var drones = List.of(
                new DroneFromSimulator(
                        "filename",
                        "server",
                        date,
                        time,
                        "DROP",
                        "id",
                        "idExt",
                        "BARDZOZLYLATITUDE",
                        "DOSLOWNIELOSOWESLOWA",
                        90,
                        100,
                        50,
                        "Nigeria",
                        "operator",
                        7,
                        "Magenta",
                        "Belmondo",
                        "420-69-2137",
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

        var result = DronesFromSimulatorMapper.map(drones);

        Assertions.assertEquals(result.size(), 0);
    }

    @Test
    public void ShouldReturnEmptyList_IfProvidedEmptyList(){
        var result = DronesFromSimulatorMapper.map(new ArrayList<>());
        Assertions.assertEquals(result.size(), 0);
    }
}
