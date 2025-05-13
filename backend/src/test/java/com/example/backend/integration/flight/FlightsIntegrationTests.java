package com.example.backend.integration.flight;

import com.example.backend.common.filtering.ComparisonType;
import com.example.backend.common.sorting.OrderType;
import com.example.backend.domain.drone.filtering.IDroneFilter;
import com.example.backend.domain.flight.FlightEntity;
import com.example.backend.domain.flight.FlightRepository;
import com.example.backend.domain.flight.FlightService;
import com.example.backend.domain.flight.filtering.FlightBooleanFilter;
import com.example.backend.domain.flight.filtering.FlightDateAndTimeFilter;
import com.example.backend.domain.flight.filtering.FlightNumberFilter;
import com.example.backend.domain.flight.filtering.FlightTimeFilter;
import com.example.backend.domain.flight.filtering.IFlightFilter;
import com.example.backend.domain.flight.sorting.FlightDateAndTimeSort;
import com.example.backend.domain.flight.sorting.FlightSort;
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
public class FlightsIntegrationTests {
    private FlightService flightService;

    @Autowired
    private TestEntityManager fakeDb;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private FlightRecordRepository flightRecordRepository;

    @BeforeEach
    public void setUp(){
        flightService = new FlightService(flightRepository, flightRecordRepository);
        setupDatabase();
    }

    @Test
    public void ShouldReturnAllDroneFlights_WhenNoFiltersAndSortApplied(){
        var result = flightService.getDroneFinishedFlights("drone1", new ArrayList<>(), Optional.empty());

        Assertions.assertEquals(2, result.size());
    }

    @Test
    public void ShouldReturnAllDroneFlights_ASC_WhenDateAndTimeSortApplied(){
        var result = flightService.getDroneFinishedFlights("drone1", new ArrayList<>(), Optional.of(new FlightDateAndTimeSort("start", OrderType.ASC)));

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(LocalDate.of(2024,10,26), result.get(0).getStartDate());
        Assertions.assertEquals(LocalDate.of(2024,10,26), result.get(1).getStartDate());
        Assertions.assertEquals(LocalTime.of(10,30,0), result.get(0).getStartTime());
        Assertions.assertEquals(LocalTime.of(14,11,0), result.get(1).getStartTime());
    }

    @Test
    public void ShouldReturnAllDroneFlights_DESC_WhenDateAndTimeSortApplied(){
        var result = flightService.getDroneFinishedFlights("drone1", new ArrayList<>(), Optional.of(new FlightDateAndTimeSort("start", OrderType.DESC)));

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(LocalDate.of(2024,10,26), result.get(0).getStartDate());
        Assertions.assertEquals(LocalDate.of(2024,10,26), result.get(1).getStartDate());
        Assertions.assertEquals(LocalTime.of(14,11,0), result.get(0).getStartTime());
        Assertions.assertEquals(LocalTime.of(10,30,0), result.get(1).getStartTime());
    }

    @Test
    public void ShouldReturnAllDroneFlights_ASC_WhenTimeSortApplied(){
        var result = flightService.getDroneFinishedFlights("drone1", new ArrayList<>(), Optional.of(new FlightSort("duration", OrderType.ASC)));

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(LocalTime.of(0,5,0), result.get(0).getDuration());
        Assertions.assertEquals(LocalTime.of(0,10,0), result.get(1).getDuration());
    }

    @Test
    public void ShouldReturnAllDroneFlights_DESC_WhenTimeSortApplied(){
        var result = flightService.getDroneFinishedFlights("drone1", new ArrayList<>(), Optional.of(new FlightSort("duration", OrderType.DESC)));

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(LocalTime.of(0,10,0), result.get(0).getDuration());
        Assertions.assertEquals(LocalTime.of(0,5,0), result.get(1).getDuration());
    }

    @Test
    public void ShouldReturnAllDroneFlights_ASC_WhenNumberSortApplied(){
        var result = flightService.getDroneFinishedFlights("drone1", new ArrayList<>(), Optional.of(new FlightSort("elevationGain", OrderType.ASC)));

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(0, result.get(0).getElevationGain());
        Assertions.assertEquals(20, result.get(1).getElevationGain());
    }

    @Test
    public void ShouldReturnAllDroneFlights_DESC_WhenNumberSortApplied(){
        var result = flightService.getDroneFinishedFlights("drone1", new ArrayList<>(), Optional.of(new FlightSort("elevationGain", OrderType.DESC)));

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(20, result.get(0).getElevationGain());
        Assertions.assertEquals(0, result.get(1).getElevationGain());
    }

    @Test
    public void ShouldReturnDroneFlights_ThatPassTheDateAndTimeFilter(){
        var filter = new FlightDateAndTimeFilter("start", "2024-10-26T14:11", ComparisonType.GreaterThanOrEqual);
        List<IFlightFilter> filters = new ArrayList<>();
        filters.add(filter);
        var result = flightService.getDroneFinishedFlights("drone1", filters, Optional.empty());

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(LocalDate.of(2024,10,26), result.get(0).getStartDate());
    }

    @Test
    public void ShouldReturnDroneFlights_ThatPassTheTimeFilter(){
        var filter = new FlightTimeFilter("duration", "06:00", ComparisonType.GreaterThanOrEqual);
        List<IFlightFilter> filters = new ArrayList<>();
        filters.add(filter);
        var result = flightService.getDroneFinishedFlights("drone1", filters, Optional.empty());

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(LocalTime.of(0,10,0), result.get(0).getDuration());
    }

    @Test
    public void ShouldReturnDroneFlights_ThatPassTheNumberFilter(){
        var filter = new FlightNumberFilter("elevationGain", 5, ComparisonType.GreaterThanOrEqual);
        List<IFlightFilter> filters = new ArrayList<>();
        filters.add(filter);
        var result = flightService.getDroneFinishedFlights("drone1", filters, Optional.empty());

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(20, result.get(0).getElevationGain());
    }

    @Test
    public void ShouldReturnDroneFlights_ThatPassTheBooleanFilter(){
        var filter = new FlightBooleanFilter("didLand", true, ComparisonType.Equals);
        List<IFlightFilter> filters = new ArrayList<>();
        filters.add(filter);
        var result = flightService.getDroneFinishedFlights("drone1", filters, Optional.empty());

        Assertions.assertEquals(1, result.size());
        Assertions.assertTrue(result.get(0).isDidLand());
    }

    private void setupDatabase(){
        fakeDb.clear();
        var drone = new DroneEntityFixtureBuilder().withRegistrationNumber("drone1").withIsAirbourne(true).build();

        fakeDb.persistAndFlush(drone);

        var flightRecordForFirstFlight1 = new FlightRecordEntityFixtureBuilder()
                .withId("flightRecord1")
                .withAltitude(10)
                .withDateAndTime(LocalDate.of(2024,10,26),LocalTime.of(10,30,0))
                .build();
        var flightRecordForFirstFlight2 = new FlightRecordEntityFixtureBuilder()
                .withId("flightRecord2")
                .withAltitude(30)
                .withDateAndTime(LocalDate.of(2024,10,26),LocalTime.of(10,35,0))
                .build();
        var flightRecordForSecondFlight1 = new FlightRecordEntityFixtureBuilder()
                .withId("flightRecord3")
                .withAltitude(1)
                .withDateAndTime(LocalDate.of(2024,10,26),LocalTime.of(14,11,0))
                .build();
        var flightRecordForSecondFlight2 = new FlightRecordEntityFixtureBuilder()
                .withId("flightRecord4")
                .withAltitude(1)
                .withDateAndTime(LocalDate.of(2024,10,26),LocalTime.of(14,21,0))
                .build();

        flightRecordForFirstFlight1.setDrone(drone);
        flightRecordForFirstFlight2.setDrone(drone);
        flightRecordForSecondFlight1.setDrone(drone);
        flightRecordForSecondFlight2.setDrone(drone);

        fakeDb.persistAndFlush(flightRecordForFirstFlight1);
        fakeDb.persistAndFlush(flightRecordForFirstFlight2);
        fakeDb.persistAndFlush(flightRecordForSecondFlight1);
        fakeDb.persistAndFlush(flightRecordForSecondFlight2);

        drone.setFlightRecords(List.of(flightRecordForFirstFlight1, flightRecordForFirstFlight2));
        drone.setFlightRecords(List.of(flightRecordForSecondFlight1, flightRecordForSecondFlight2));

        fakeDb.persistAndFlush(drone);
        fakeDb.persistAndFlush(drone);

        var flight1 = new FlightEntity();
        var flight2 = new FlightEntity();

        fakeDb.persistAndFlush(flight1);
        fakeDb.persistAndFlush(flight2);

        flight1.summarizeFlight(List.of(flightRecordForFirstFlight1, flightRecordForFirstFlight2), true);
        flight2.summarizeFlight(List.of(flightRecordForSecondFlight1, flightRecordForSecondFlight2), false);

        flightRecordForFirstFlight1.setFlight(flight1);
        flightRecordForFirstFlight2.setFlight(flight1);
        flightRecordForSecondFlight1.setFlight(flight2);
        flightRecordForSecondFlight2.setFlight(flight2);

        fakeDb.persistAndFlush(flight1);
        fakeDb.persistAndFlush(flight2);
        fakeDb.persistAndFlush(flightRecordForFirstFlight1);
        fakeDb.persistAndFlush(flightRecordForFirstFlight2);
        fakeDb.persistAndFlush(flightRecordForSecondFlight1);
        fakeDb.persistAndFlush(flightRecordForSecondFlight2);
    }
}
