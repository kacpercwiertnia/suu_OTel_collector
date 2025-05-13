package com.example.backend.integration.drone;

import com.example.backend.common.filtering.ComparisonType;
import com.example.backend.common.sorting.OrderType;
import com.example.backend.domain.drone.DroneRepository;
import com.example.backend.domain.drone.DroneService;
import com.example.backend.domain.drone.filtering.DroneTextFilter;
import com.example.backend.domain.drone.filtering.IDroneFilter;
import com.example.backend.domain.drone.sorting.DroneSort;
import com.example.backend.domain.flight.FlightRepository;
import com.example.backend.domain.flightRecord.FlightRecordRepository;
import com.example.backend.unit.domain.drone.DroneEntityFixtureBuilder;
import com.example.backend.unit.domain.flightRecord.FlightRecordEntityFixtureBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class DronesIntegrationTests {
    private DroneService droneService;

    @Autowired
    private TestEntityManager fakeDb;
    @Autowired
    private DroneRepository droneRepository;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private FlightRecordRepository flightRecordRepository;

    @BeforeEach
    public void setUp(){
        droneService = new DroneService(droneRepository, flightRecordRepository, flightRepository);
        setupDatabase();
    }

    @Test
    public void ShouldReturnAllDrones_WhenNoFiltersAndSortApplied(){
        var result = droneService.getDrones(new ArrayList<>(), Optional.empty());

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("flyingDroneWithRecords", result.get(0).getRegistrationNumber());
        Assertions.assertEquals("notFlyingDroneWithRecords", result.get(1).getRegistrationNumber());
    }

    @Test
    public void ShouldReturnAllDrones_ASC_WhenTextSortApplied(){
        var result = droneService.getDrones(new ArrayList<>(), Optional.of(new DroneSort("registrationNumber", OrderType.ASC)));

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("flyingDroneWithRecords", result.get(0).getRegistrationNumber());
        Assertions.assertEquals("notFlyingDroneWithRecords", result.get(1).getRegistrationNumber());
    }

    @Test
    public void ShouldReturnAllDrones_DESC_WhenTextSortApplied(){
        var result = droneService.getDrones(new ArrayList<>(), Optional.of(new DroneSort("registrationNumber", OrderType.DESC)));

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("notFlyingDroneWithRecords", result.get(0).getRegistrationNumber());
        Assertions.assertEquals("flyingDroneWithRecords", result.get(1).getRegistrationNumber());
    }

    @Test
    public void ShouldReturnAllDrones_ASC_WhenNumberSortApplied(){
        var result = droneService.getDrones(new ArrayList<>(), Optional.of(new DroneSort("recentAltitude", OrderType.ASC)));

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("notFlyingDroneWithRecords", result.get(0).getRegistrationNumber());
        Assertions.assertEquals("flyingDroneWithRecords", result.get(1).getRegistrationNumber());
    }

    @Test
    public void ShouldReturnAllDrones_DESC_WhenNumberSortApplied(){
        var result = droneService.getDrones(new ArrayList<>(), Optional.of(new DroneSort("recentAltitude", OrderType.DESC)));

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("flyingDroneWithRecords", result.get(0).getRegistrationNumber());
        Assertions.assertEquals("notFlyingDroneWithRecords", result.get(1).getRegistrationNumber());
    }

    @Test
    public void ShouldReturnDrones_WithRegisteredPositions_ThatPassTheFilter_CaseInsensitive(){
        var filter = new DroneTextFilter("registrationNumber", "flyingDroneWithRecords", ComparisonType.Equals);
        List<IDroneFilter> filters = new ArrayList<>();
        filters.add(filter);
        var result = droneService.getDrones(filters, Optional.empty());

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("flyingDroneWithRecords", result.get(0).getRegistrationNumber());
    }

    private void setupDatabase(){
        persistDroneWithFlightRecord("1", "flyingDroneWithRecords", true, 100);
        persistDroneWithFlightRecord("2", "notFlyingDroneWithRecords", false, 10);
    }

    private void persistDroneWithFlightRecord(String flightRecordId, String droneRegNumber, boolean isFlying, int altitude){
        var flightRecord = new FlightRecordEntityFixtureBuilder()
                .withId(flightRecordId)
                .withDateAndTime(LocalDate.now(), LocalTime.now())
                .withAltitude(altitude)
                .build();
        fakeDb.persistAndFlush(flightRecord);

        var drone = new DroneEntityFixtureBuilder()
                .withRegistrationNumber(droneRegNumber)
                .withFlyingRecords(List.of(flightRecord))
                .withIsAirbourne(isFlying).build();
        fakeDb.persistAndFlush(drone);

        flightRecord.setDrone(drone);
        fakeDb.persistAndFlush(flightRecord);
    }
}
