package com.example.backend.unit.simulatorIntegration;

import com.example.backend.simulatorIntegration.DroneRecordsCsvReader;
import com.example.backend.simulatorIntegration.model.DroneFromSimulator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DroneRecordsCsvReaderTests {
    private DroneRecordsCsvReader sut;
    private DummyFilePathProvider pathProvider;

    @BeforeEach
    public void setUp(){
        sut = new DroneRecordsCsvReader();
        pathProvider = new DummyFilePathProvider();
    }

    @Test
    public void ShouldReturnParsedRecords(){
        String path = pathProvider.getPath();

        List<DroneFromSimulator> result = sut.parseCsv(path);

        var expectedDroneOne = new DroneFromSimulator(
                "file3",
                "Server1",
                LocalDate.of(2022, 2, 15),
                LocalTime.of(14, 52, 1, 514200000),
                "UPD",
                "0000002",
                "BOB4321", //TO SIE ZMIENIA
                "513027N",
                "0220713E",
                342,
                30,
                400,
                "Polish",
                "UK",
                2,
                "Magenta",
                "DJI Air 2S",
                "XDA123", //TO SIE ZMIENIA,
                "UK501",
                "Airborne",
                593,
                "Mode S",
                "1000",
                "512027N",
                "0220913E",
                "Mobile1");

        var expectedDroneTwo = new DroneFromSimulator(
                "file3",
                "Server1",
                LocalDate.of(2022, 2, 15),
                LocalTime.of(14, 52, 1, 514200000),
                "UPD",
                "0000002",
                "DAB12345",
                "513027N",
                "0220713E",
                342,
                30,
                400,
                "Polish",
                "UK",
                2,
                "Magenta",
                "DJI Air 2S",
                "WOL423",
                "UK501",
                "Airborne",
                593,
                "Mode S",
                "1000",
                "512027N",
                "0220913E",
                "Mobile1");

        var expectedDroneThree = new DroneFromSimulator(
                "file3",
                "Server1",
                LocalDate.of(2022, 2, 15),
                LocalTime.of(14, 52, 1, 514200000),
                "UPD",
                "0000002",
                "DAB12345",
                "513027N",
                "0220713E",
                342,
                30,
                400,
                "Polish",
                "UK",
                2,
                "Magenta",
                "DJI Air 2S",
                "JON423",
                "UK501",
                "Airborne",
                593,
                "Mode S",
                "1000",
                "512027N",
                "0220913E",
                "Mobile1");

            Assertions.assertEquals(List.of(expectedDroneOne, expectedDroneTwo, expectedDroneThree), result);
    }
}
