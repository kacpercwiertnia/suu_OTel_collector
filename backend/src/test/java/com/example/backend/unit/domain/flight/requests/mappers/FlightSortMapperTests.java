package com.example.backend.unit.domain.flight.requests.mappers;

import com.example.backend.common.sorting.dtos.SortEntry;
import com.example.backend.domain.flight.requests.mappers.FlightSortMapper;
import com.example.backend.domain.flight.sorting.FlightSort;
import com.example.backend.domain.flight.sorting.IFlightSort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

public class FlightSortMapperTests {
    @ParameterizedTest
    @ValueSource(strings = {"ASC", "DESC"})
    public void ShouldProperlyMap_Sort(String orderType){
        var sortEntry = new SortEntry("registrationNumber", "whatever", orderType);

        Optional<IFlightSort> result = FlightSortMapper.map(sortEntry);

        Assertions.assertTrue(result.isPresent());

        var sort = result.get();

        Assertions.assertEquals(sort.getClass(), FlightSort.class);
    }
}
