package com.example.backend.domain.drone.requests.mappers;

import com.example.backend.common.sorting.OrderType;
import com.example.backend.common.sorting.dtos.SortEntry;
import com.example.backend.domain.drone.sorting.DroneSort;
import com.example.backend.domain.drone.sorting.IDroneSort;

import java.util.Optional;

public class DroneSortMapper {
    public static Optional<IDroneSort> map(SortEntry sort){
        return sort != null ? Optional.of(new DroneSort(sort.parameter(), Enum.valueOf(OrderType.class, sort.orderType()))) : Optional.empty();
    }
}
