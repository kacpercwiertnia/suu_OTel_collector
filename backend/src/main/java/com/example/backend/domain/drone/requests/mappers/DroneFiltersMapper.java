package com.example.backend.domain.drone.requests.mappers;

import com.example.backend.common.filtering.ComparisonType;
import com.example.backend.domain.drone.filtering.IDroneFilter;
import com.example.backend.domain.drone.filtering.DroneNumberFilter;
import com.example.backend.domain.drone.filtering.DroneTextFilter;
import com.example.backend.common.filtering.dtos.NumberFilterEntry;
import com.example.backend.common.filtering.dtos.TextFilterEntry;

import java.util.ArrayList;
import java.util.List;

public class DroneFiltersMapper {
    public static List<IDroneFilter> map(List<TextFilterEntry> textFilters, List<NumberFilterEntry> numberFilters) throws IllegalArgumentException{
        List<IDroneFilter> result = new ArrayList<>();

        result.addAll(textFilters.stream().map(filter -> new DroneTextFilter(
                filter.parameter(), filter.value(), Enum.valueOf(ComparisonType.class, filter.comparisonType())
        )).toList());

        result.addAll(numberFilters.stream().map(filter -> new DroneNumberFilter(
                filter.parameter(), filter.value(), Enum.valueOf(ComparisonType.class, filter.comparisonType())
        )).toList());

        return result;
    }

}
