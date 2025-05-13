package com.example.backend.unit.domain.flight.sorting;

import com.example.backend.common.sorting.OrderType;
import com.example.backend.domain.flight.sorting.FlightSort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class FlightSortTests {
    @ParameterizedTest
    @EnumSource(OrderType.class)
    public void ShouldCreateSort(OrderType orderType){
        var result = new FlightSort("attribute", orderType);

        Assertions.assertNotNull(result);
    }
}
