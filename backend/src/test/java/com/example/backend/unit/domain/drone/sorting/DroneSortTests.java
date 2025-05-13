package com.example.backend.unit.domain.drone.sorting;

import com.example.backend.common.sorting.OrderType;
import com.example.backend.domain.drone.sorting.DroneSort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class DroneSortTests {
    @ParameterizedTest
    @EnumSource(OrderType.class)
    public void ShouldCreateSort(OrderType orderType){
        var result = new DroneSort("attribute", orderType);

        Assertions.assertNotNull(result);
    }
}
