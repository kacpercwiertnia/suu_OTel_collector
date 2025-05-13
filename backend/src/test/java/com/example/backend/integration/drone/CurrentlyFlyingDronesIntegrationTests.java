package com.example.backend.integration.drone;

import com.example.backend.common.sorting.OrderType;
import com.example.backend.domain.drone.DroneRepository;
import com.example.backend.domain.drone.DroneService;
import com.example.backend.common.filtering.ComparisonType;
import com.example.backend.domain.drone.filtering.IDroneFilter;
import com.example.backend.domain.drone.filtering.DroneNumberFilter;
import com.example.backend.domain.drone.filtering.DroneTextFilter;
import com.example.backend.domain.drone.sorting.DroneSort;
import com.example.backend.domain.flight.FlightRepository;
import com.example.backend.domain.flightRecord.FlightRecordEntity;
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
public class CurrentlyFlyingDronesIntegrationTests {
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
    }

    @Test
    public void ShouldReturnAllCurrentlyFlyingDrones_WithRegisteredPositions_WhenNoFiltersAndSortApplied(){
        setupDatabase(
                List.of(new FlightRecordEntityFixtureBuilder().withId("1").build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("2").build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("3").build())
        );
        var result = droneService.getCurrentlyFlyingDrones(new ArrayList<>(), Optional.empty());

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("flyingDroneToBeFound", result.get(0).getRegistrationNumber());
        Assertions.assertEquals("flyingDroneNotToBeFound", result.get(1).getRegistrationNumber());
    }

    @Test
    public void ShouldReturnAllCurrentlyFlyingDrones_WithRegisteredPositions_ASC_WhenTextSortApplied(){
        setupDatabase(
                List.of(new FlightRecordEntityFixtureBuilder().withId("1").build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("2").build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("3").build())
        );
        var result = droneService.getCurrentlyFlyingDrones(new ArrayList<>(), Optional.of(new DroneSort("registrationNumber", OrderType.ASC)));

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("flyingDroneNotToBeFound", result.get(0).getRegistrationNumber());
        Assertions.assertEquals("flyingDroneToBeFound", result.get(1).getRegistrationNumber());
    }

    @Test
    public void ShouldReturnAllCurrentlyFlyingDrones_WithRegisteredPositions_DESC_WhenTextSortApplied(){
        setupDatabase(
                List.of(new FlightRecordEntityFixtureBuilder().withId("1").build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("2").build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("3").build())
        );
        var result = droneService.getCurrentlyFlyingDrones(new ArrayList<>(), Optional.of(new DroneSort("registrationNumber", OrderType.DESC)));

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("flyingDroneToBeFound", result.get(0).getRegistrationNumber());
        Assertions.assertEquals("flyingDroneNotToBeFound", result.get(1).getRegistrationNumber());
    }

    @Test
    public void ShouldReturnAllCurrentlyFlyingDrones_WithRegisteredPositions_ASC_WhenNumberSortApplied(){
        setupDatabase(
                List.of(new FlightRecordEntityFixtureBuilder().withId("1").withAltitude(100).build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("2").withAltitude(10).build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("3").build())
        );
        var result = droneService.getCurrentlyFlyingDrones(new ArrayList<>(), Optional.of(new DroneSort("recentAltitude", OrderType.ASC)));

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("flyingDroneNotToBeFound", result.get(0).getRegistrationNumber());
        Assertions.assertEquals("flyingDroneToBeFound", result.get(1).getRegistrationNumber());
    }

    @Test
    public void ShouldReturnAllCurrentlyFlyingDrones_WithRegisteredPositions_DESC_WhenNumberSortApplied(){
        setupDatabase(
                List.of(new FlightRecordEntityFixtureBuilder().withId("1").withAltitude(100).build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("2").withAltitude(10).build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("3").build())
        );
        var result = droneService.getCurrentlyFlyingDrones(new ArrayList<>(), Optional.of(new DroneSort("recentAltitude", OrderType.DESC)));

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("flyingDroneToBeFound", result.get(0).getRegistrationNumber());
        Assertions.assertEquals("flyingDroneNotToBeFound", result.get(1).getRegistrationNumber());
    }

    @Test
    public void ShouldReturnAllCurrentlyFlyingDrones_ThatPassTheNumberFilter_BasedOnTheFlightRecordsTime(){
        //prepare records in db
        var date = LocalDate.now();
        setupDatabase(
                List.of(
                        new FlightRecordEntityFixtureBuilder().withId("1").withDateAndTime(
                                date,
                                LocalTime.of(5, 5, 5, 5)
                        )
                                .withAltitude(30)
                                .build(),
                        new FlightRecordEntityFixtureBuilder().withId("2").withDateAndTime(
                                        date,
                                        LocalTime.of(4, 4, 4, 4)
                                )
                                .withAltitude(5)
                                .build()
                ),
                List.of(
                        new FlightRecordEntityFixtureBuilder().withId("3").withDateAndTime(
                                        date,
                                        LocalTime.of(5, 5, 5, 5)
                                )
                                .withAltitude(5)
                                .build(),
                        new FlightRecordEntityFixtureBuilder().withId("4").withDateAndTime(
                                        date,
                                        LocalTime.of(4, 4, 4, 4)
                                )
                                .withAltitude(30)
                                .build()
                ),
                List.of(
                        new FlightRecordEntityFixtureBuilder().withId("5").withDateAndTime(
                                        date,
                                        LocalTime.of(5, 5, 5, 5)
                                )
                                .withAltitude(30)
                                .build(),
                        new FlightRecordEntityFixtureBuilder().withId("6").withDateAndTime(
                                        date,
                                        LocalTime.of(4, 4, 4, 4)
                                )
                                .withAltitude(5)
                                .build()
                )
        );

        //prepare filter
        var filter = new DroneNumberFilter("recentAltitude", 10, ComparisonType.GreaterThanOrEqual);

        //act
        var result = droneService.getCurrentlyFlyingDrones(List.of(filter), Optional.empty());

        //assert
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("flyingDroneToBeFound", result.get(0).getRegistrationNumber());
    }

    @Test
    public void ShouldReturnAllCurrentlyFlyingDrones_ThatPassTheNumberFilter_BasedOnTheFlightRecordsDate(){
        //prepare records in db
        var time = LocalTime.now();
        setupDatabase(
                List.of(
                        new FlightRecordEntityFixtureBuilder().withId("1").withDateAndTime(
                                        LocalDate.of(2024, 10, 10),
                                        time
                                )
                                .withAltitude(30)
                                .build(),
                        new FlightRecordEntityFixtureBuilder().withId("2").withDateAndTime(
                                        LocalDate.of(2023, 5, 5),
                                        time
                                )
                                .withAltitude(5)
                                .build()
                ),
                List.of(
                        new FlightRecordEntityFixtureBuilder().withId("3").withDateAndTime(
                                        LocalDate.of(2024, 10, 10),
                                        time
                                )
                                .withAltitude(4)
                                .build(),
                        new FlightRecordEntityFixtureBuilder().withId("4").withDateAndTime(
                                        LocalDate.of(2023, 5, 5),
                                        time
                                )
                                .withAltitude(30)
                                .build()
                ),
                List.of(
                        new FlightRecordEntityFixtureBuilder().withId("5").withDateAndTime(
                                        LocalDate.of(2024, 10, 10),
                                        time
                                )
                                .withAltitude(30)
                                .build(),
                        new FlightRecordEntityFixtureBuilder().withId("6").withDateAndTime(
                                        LocalDate.of(2023, 5, 5),
                                        time
                                )
                                .withAltitude(5)
                                .build()
                )
        );

        var filter = new DroneNumberFilter("recentAltitude", 10, ComparisonType.GreaterThanOrEqual);

        var result = droneService.getCurrentlyFlyingDrones(List.of(filter), Optional.empty());

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("flyingDroneToBeFound", result.get(0).getRegistrationNumber());
    }

    @Test
    public void ShouldReturnCurrentlyFlyingDrones_WithRegisteredPositions_ThatPassTheRegistrationNumberFilter_CaseInsensitive(){
        setupDatabase(
                List.of(new FlightRecordEntityFixtureBuilder().withId("1").build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("2").build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("3").build())
        );

        var filter = new DroneTextFilter("registrationNumber", "flyingdronetobefound", ComparisonType.Contains);
        List<IDroneFilter> filters = new ArrayList<>();
        filters.add(filter);
        var result = droneService.getCurrentlyFlyingDrones(filters, Optional.empty());

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("flyingDroneToBeFound", result.get(0).getRegistrationNumber());
    }

    @Test
    public void ShouldReturnCurrentlyFlyingDrones_WithRegisteredPositions_ThatPassTheAltitudeMinFilter(){
        //prepare database
        setupDatabase(
                List.of(new FlightRecordEntityFixtureBuilder().withId("1").withAltitude(30).build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("2").withAltitude(5).build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("3").withAltitude(30).build())
        );

        var filter = new DroneNumberFilter("recentAltitude", 10, ComparisonType.GreaterThanOrEqual);

        //act
        var result = droneService.getCurrentlyFlyingDrones(List.of(filter), Optional.empty());

        //assert
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("flyingDroneToBeFound", result.get(0).getRegistrationNumber());
    }

    @Test
    public void ShouldReturnCurrentlyFlyingDrones_WithRegisteredPositions_ThatPassTheAltitudeMaxFilter(){
        //prepare database
        setupDatabase(
                List.of(new FlightRecordEntityFixtureBuilder().withId("1").withAltitude(5).build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("2").withAltitude(50).build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("3").withAltitude(5).build())
        );

        var filter = new DroneNumberFilter("recentAltitude", 10, ComparisonType.LesserThanOrEqual);

        //act
        var result = droneService.getCurrentlyFlyingDrones(List.of(filter), Optional.empty());

        //assert
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("flyingDroneToBeFound", result.get(0).getRegistrationNumber());
    }
    @Test
    public void ShouldReturnCurrentlyFlyingDrones_WithRegisteredPositions_ThatPassTheLatitudeMinFilter(){
        //prepare database
        setupDatabase(
                List.of(new FlightRecordEntityFixtureBuilder().withId("1").withLatitude(30).build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("2").withLatitude(5).build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("3").withLatitude(30).build())
        );

        var filter = new DroneNumberFilter("recentLatitude", 10, ComparisonType.GreaterThanOrEqual);

        //act
        var result = droneService.getCurrentlyFlyingDrones(List.of(filter), Optional.empty());

        //assert
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("flyingDroneToBeFound", result.get(0).getRegistrationNumber());
    }

    @Test
    public void ShouldReturnCurrentlyFlyingDrones_WithRegisteredPositions_ThatPassTheLatitudeMaxFilter(){
        //prepare database
        setupDatabase(
                List.of(new FlightRecordEntityFixtureBuilder().withId("1").withLatitude(5).build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("2").withLatitude(50).build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("3").withLatitude(5).build())
        );

        var filter = new DroneNumberFilter("recentLatitude", 10, ComparisonType.LesserThanOrEqual);

        //act
        var result = droneService.getCurrentlyFlyingDrones(List.of(filter), Optional.empty());

        //assert
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("flyingDroneToBeFound", result.get(0).getRegistrationNumber());
    }

    @Test
    public void ShouldReturnCurrentlyFlyingDrones_WithRegisteredPositions_ThatPassTheLongitudeMinFilter(){
        //prepare database
        setupDatabase(
                List.of(new FlightRecordEntityFixtureBuilder().withId("1").withLongitude(30).build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("2").withLongitude(5).build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("3").withLongitude(10.88).build())
        );

        var filter = new DroneNumberFilter("recentLongitude", 10.88, ComparisonType.GreaterThanOrEqual);

        //act
        var result = droneService.getCurrentlyFlyingDrones(List.of(filter), Optional.empty());

        //assert
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("flyingDroneToBeFound", result.get(0).getRegistrationNumber());
    }

    @Test
    public void ShouldReturnCurrentlyFlyingDrones_WithRegisteredPositions_ThatPassTheLongitudeMaxFilter(){
        //prepare database
        setupDatabase(
                List.of(new FlightRecordEntityFixtureBuilder().withId("1").withLongitude(5).build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("2").withLongitude(50).build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("3").withLongitude(5.5).build())
        );

        var filter = new DroneNumberFilter("recentLongitude", 5.5, ComparisonType.LesserThanOrEqual);

        //act
        var result = droneService.getCurrentlyFlyingDrones(List.of(filter), Optional.empty());

        //assert
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("flyingDroneToBeFound", result.get(0).getRegistrationNumber());
    }

    @Test
    public void ShouldReturnCurrentlyFlyingDrones_WithRegisteredPositions_ThatPassTheFuelMinFilter(){
        //prepare database
        setupDatabase(
                List.of(new FlightRecordEntityFixtureBuilder().withId("1").withFuel(30).build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("2").withFuel(5).build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("3").withFuel(30).build())
        );

        var filter = new DroneNumberFilter("recentFuel", 10, ComparisonType.GreaterThanOrEqual);

        //act
        var result = droneService.getCurrentlyFlyingDrones(List.of(filter), Optional.empty());

        //assert
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("flyingDroneToBeFound", result.get(0).getRegistrationNumber());
    }

    @Test
    public void ShouldReturnCurrentlyFlyingDrones_WithRegisteredPositions_ThatPassTheFuelMaxFilter(){
        //prepare database
        setupDatabase(
                List.of(new FlightRecordEntityFixtureBuilder().withId("1").withFuel(5).build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("2").withFuel(50).build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("3").withFuel(5).build())
        );

        var filter = new DroneNumberFilter("recentFuel", 10, ComparisonType.LesserThanOrEqual);

        //act
        var result = droneService.getCurrentlyFlyingDrones(List.of(filter), Optional.empty());

        //assert
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("flyingDroneToBeFound", result.get(0).getRegistrationNumber());
    }

    @Test
    public void ShouldReturnCurrentlyFlyingDrones_WithRegisteredPositions_ThatPassTheModelFilter_CaseInsensitive(){
        //prepare database
        setupDatabase(
                List.of(new FlightRecordEntityFixtureBuilder().withId("1").build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("2").build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("3").build())
        );

        var filter = new DroneTextFilter("model", "MODELTOBEFOUND", ComparisonType.Contains);

        //act
        var result = droneService.getCurrentlyFlyingDrones(List.of(filter), Optional.empty());

        //assert
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("flyingDroneToBeFound", result.get(0).getRegistrationNumber());
    }

    @Test
    public void ShouldReturnCurrentlyFlyingDrones_WithRegisteredPositions_ThatPassTheOperatorFilter_CaseInsensitive(){
        //prepare database
        setupDatabase(
                List.of(new FlightRecordEntityFixtureBuilder().withId("1").build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("2").build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("3").build())
        );

        var filter = new DroneTextFilter("operator", "operatorToBeFound", ComparisonType.Contains);

        //act
        var result = droneService.getCurrentlyFlyingDrones(List.of(filter), Optional.empty());

        //assert
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("flyingDroneToBeFound", result.get(0).getRegistrationNumber());
    }

    @Test
    public void ShouldReturnCurrentlyFlyingDrones_WithRegisteredPositions_ThatPassTheTypeFilter_CaseInsensitive(){
        //prepare database
        setupDatabase(
                List.of(new FlightRecordEntityFixtureBuilder().withId("1").build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("2").build()),
                List.of(new FlightRecordEntityFixtureBuilder().withId("3").build())
        );

        var filter = new DroneTextFilter("type", "airborne", ComparisonType.Contains);

        //act
        var result = droneService.getCurrentlyFlyingDrones(List.of(filter), Optional.empty());

        //assert
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("flyingDroneToBeFound", result.get(0).getRegistrationNumber());
    }


    private void setupDatabase(List<FlightRecordEntity> flightRecordsToBeFound,
                               List<FlightRecordEntity> flightRecordsNotToBeFound,
                               List<FlightRecordEntity> flightRecordsForNotFlyingDrone
    ){
        persistDroneWithFlightRecord(flightRecordsToBeFound, "flyingDroneToBeFound",
                true, "modelToBeFound", "operatorToBeFound", "Airborne");
        persistDroneWithFlightRecord(flightRecordsNotToBeFound, "flyingDroneNotToBeFound",
                true, "modelNotToBeFound", "operatorNotToBeFound", "Grounded");

        var flyingDroneWithNoRecord = new DroneEntityFixtureBuilder().withRegistrationNumber("flyingDroneWithNoRecords").build();
        fakeDb.persistAndFlush(flyingDroneWithNoRecord);

        persistDroneWithFlightRecord(flightRecordsForNotFlyingDrone, "notFlyingDroneWithRecords",
                false, "modelToBeFound", "operatorToBeFound", "Grounded");
    }

    private void persistDroneWithFlightRecord(List<FlightRecordEntity> flightRecords,
                                              String droneRegNumber,
                                              boolean isFlying,
                                              String model,
                                              String operator,
                                              String type){
        for (var flightRecord : flightRecords){
            fakeDb.persistAndFlush(flightRecord);
        }

        var drone = new DroneEntityFixtureBuilder()
                .withRegistrationNumber(droneRegNumber)
                .withIsAirbourne(isFlying)
                .withFlyingRecords(flightRecords)
                .withModel(model)
                .withOperator(operator)
                .withType(type)
                .build();

        fakeDb.persistAndFlush(drone);

        for (var flightRecord : flightRecords){
            flightRecord.setDrone(drone);
            fakeDb.persistAndFlush(flightRecord);
        }
    }
}
