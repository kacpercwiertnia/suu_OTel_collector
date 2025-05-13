package com.example.backend.domain.flight.requests.mappers;

import com.example.backend.common.filtering.ComparisonType;
import com.example.backend.common.filtering.dtos.BooleanFilterEntry;
import com.example.backend.common.filtering.dtos.DateAndTimeFilterEntry;
import com.example.backend.common.filtering.dtos.NumberFilterEntry;
import com.example.backend.common.filtering.dtos.TimeFilterEntry;
import com.example.backend.domain.flight.filtering.FlightBooleanFilter;
import com.example.backend.domain.flight.filtering.FlightDateAndTimeFilter;
import com.example.backend.domain.flight.filtering.FlightNumberFilter;
import com.example.backend.domain.flight.filtering.FlightTimeFilter;
import com.example.backend.domain.flight.filtering.IFlightFilter;

import java.util.ArrayList;
import java.util.List;

public class FlightFiltersMapper {
    public static List<IFlightFilter> map(List<DateAndTimeFilterEntry> dateAndtimeFilters,
                                          List<NumberFilterEntry> numberFilters,
                                          List<BooleanFilterEntry> booleanFilters,
                                          List<TimeFilterEntry> timeFilters) throws IllegalArgumentException{
        List<IFlightFilter> result = new ArrayList<>();

        result.addAll(dateAndtimeFilters.stream().map(filter -> new FlightDateAndTimeFilter(
                filter.parameter(), filter.value(), Enum.valueOf(ComparisonType.class, filter.comparisonType())
        )).toList());

        result.addAll(numberFilters.stream().map(filter -> new FlightNumberFilter(
                filter.parameter(), filter.value(), Enum.valueOf(ComparisonType.class, filter.comparisonType())
        )).toList());

        result.addAll(booleanFilters.stream().map(filter -> new FlightBooleanFilter(
                filter.parameter(), filter.value(), Enum.valueOf(ComparisonType.class, filter.comparisonType())
        )).toList());

        result.addAll(timeFilters.stream().map(filter -> new FlightTimeFilter(
                filter.parameter(), filter.value(), Enum.valueOf(ComparisonType.class, filter.comparisonType())
        )).toList());

        return result;
    }
}
