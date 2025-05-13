package com.example.backend.unit.domain.drone.requests.mappers;

import com.example.backend.common.sorting.dtos.SortEntry;
import com.example.backend.domain.drone.requests.mappers.DroneSortMapper;
import com.example.backend.domain.drone.sorting.DroneSort;
import com.example.backend.domain.drone.sorting.IDroneSort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

public class DroneSortMapperTests {
    @ParameterizedTest
    @ValueSource(strings = {"ASC", "DESC"})
    public void ShouldProperlyMap_Sort(String orderType){
        var sortEntry = new SortEntry("registrationNumber", "whatever", orderType);

        Optional<IDroneSort> result = DroneSortMapper.map(sortEntry);

        Assertions.assertTrue(result.isPresent());

        var sort = result.get();

        Assertions.assertEquals(sort.getClass(), DroneSort.class);
    }
}
