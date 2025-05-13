package com.example.backend.unit.domain.drone.requests.currentlyFlyingDrones;

import com.example.backend.domain.drone.DroneEntity;
import com.example.backend.domain.drone.DroneRepository;
import com.example.backend.common.filtering.ComparisonType;
import com.example.backend.domain.drone.filtering.DroneTextFilter;
import com.example.backend.domain.drone.requests.currentlyFlyingDrones.CurrentlyFlyingDronesQuery;
import com.example.backend.unit.domain.drone.DroneEntityFixtureBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
public class CurrentlyFlyingDronesQueryTests {
    private CurrentlyFlyingDronesQuery sut;
    @Autowired
    private TestEntityManager fakeDb;
    @Autowired
    private DroneRepository droneRepository;

    @BeforeEach
    public void setUp(){
        sut = new CurrentlyFlyingDronesQuery(droneRepository);
        setupDatabase();
    }

    @Test
    public void ShouldReturnAllFlyingDrones_WhenNoFiltersApplied(){
        var result = sut.execute(new ArrayList<>());
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("dron1", result.get(0).getRegistrationNumber());
        Assertions.assertEquals("dron2", result.get(1).getRegistrationNumber());
    }

    @Test
    public void ShouldNotReturnNoFlyingDrones(){
        //prepare
        var notFlyingDrone = new DroneEntityFixtureBuilder().withRegistrationNumber("dron3").withIsAirbourne(false).build();
        fakeDb.persistAndFlush(notFlyingDrone);

        //act
        var result = sut.execute(new ArrayList<>());

        //assert
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("dron1", result.get(0).getRegistrationNumber());
        Assertions.assertTrue(result.get(0).isAirborne());
        Assertions.assertEquals("dron2", result.get(1).getRegistrationNumber());
        Assertions.assertTrue(result.get(1).isAirborne());

    }

    @Test
    public void ShouldReturnFilteredDrones_CaseInsensitive(){
        var filter = new DroneTextFilter("registrationNumber", "DRON1", ComparisonType.Equals).toSpecification();
        List<Specification<DroneEntity>> filtersList = new ArrayList<>();
        filtersList.add(filter);

        var result = sut.execute(filtersList);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("dron1", result.get(0).getRegistrationNumber());
    }

    private void setupDatabase(){
        var drone = new DroneEntityFixtureBuilder().withRegistrationNumber("dron1").withIsAirbourne(true).build();
        var drone2 = new DroneEntityFixtureBuilder().withRegistrationNumber("dron2").withIsAirbourne(true).build();

        fakeDb.persistAndFlush(drone);
        fakeDb.persistAndFlush(drone2);
    }
}
