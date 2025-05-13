package com.example.backend.unit.domain.flight.requests.mappers;

import com.example.backend.common.filtering.dtos.BooleanFilterEntry;
import com.example.backend.common.filtering.dtos.DateAndTimeFilterEntry;
import com.example.backend.common.filtering.dtos.NumberFilterEntry;
import com.example.backend.common.filtering.dtos.TimeFilterEntry;
import com.example.backend.domain.flight.filtering.FlightBooleanFilter;
import com.example.backend.domain.flight.filtering.FlightDateAndTimeFilter;
import com.example.backend.domain.flight.filtering.FlightNumberFilter;
import com.example.backend.domain.flight.filtering.FlightTimeFilter;
import com.example.backend.domain.flight.requests.mappers.FlightFiltersMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

public class FlightFiltersMapperTests {
    @ParameterizedTest
    @ValueSource(strings = {"Equals", "GreaterThanOrEqual", "LesserThanOrEqual"})
    public void ShouldProperlyMap_NumberFilter(String comparisonType){
        var numberFilterEntry = new NumberFilterEntry("registrationNumber", "whatever", 69, comparisonType);

        var result = FlightFiltersMapper.map(new ArrayList<>(), List.of(numberFilterEntry), new ArrayList<>(), new ArrayList<>());

        Assertions.assertEquals(1, result.size());
        var filter = result.get(0);
        Assertions.assertEquals(filter.getClass(), FlightNumberFilter.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ZlyEnum", "Contains"})
    public void ShouldThrowException_WhenInvalidNumberFilter(String comparisonType){
        var numberFilterEntry = new NumberFilterEntry("registrationNumber", "whatever", 69, comparisonType);

        Executable action = () -> FlightFiltersMapper.map(new ArrayList<>(), List.of(numberFilterEntry), new ArrayList<>(), new ArrayList<>());

        Assertions.assertThrows(IllegalArgumentException.class, action);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Equals"})
    public void ShouldProperlyMap_BooleanFilter(String comparisonType){
        var booleanFilterEntry = new BooleanFilterEntry("registrationNumber", "whatever", false, comparisonType);

        var result = FlightFiltersMapper.map(new ArrayList<>(), new ArrayList<>(),  List.of(booleanFilterEntry), new ArrayList<>());

        Assertions.assertEquals(1, result.size());
        var filter = result.get(0);
        Assertions.assertEquals(filter.getClass(), FlightBooleanFilter.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"GreaterThanOrEqual", "Contains", "LesserThanOrEqual"})
    public void ShouldThrowException_WhenInvalidBooleanFilter(String comparisonType){
        var booleanFilterEntry = new BooleanFilterEntry("registrationNumber", "whatever", false, comparisonType);

        Executable action = () -> FlightFiltersMapper.map(new ArrayList<>(), new ArrayList<>(),  List.of(booleanFilterEntry), new ArrayList<>());

        Assertions.assertThrows(IllegalArgumentException.class, action);
    }

    @ParameterizedTest
    @ValueSource(strings = {"GreaterThanOrEqual", "LesserThanOrEqual"})
    public void ShouldProperlyMap_DateAndTimeFilter(String comparisonType){
        var dateAndTimeFilterEntry = new DateAndTimeFilterEntry("registrationNumber", "whatever", "whatever", comparisonType);

        var result = FlightFiltersMapper.map(List.of(dateAndTimeFilterEntry), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Assertions.assertEquals(1, result.size());
        var filter = result.get(0);
        Assertions.assertEquals(filter.getClass(), FlightDateAndTimeFilter.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Equals", "Contains"})
    public void ShouldThrowException_WhenInvalidDateAndTimeFilter(String comparisonType){
        var dateAndTimeFilterEntry = new DateAndTimeFilterEntry("registrationNumber", "whatever", "whatever", comparisonType);

        Executable action = () -> FlightFiltersMapper.map(List.of(dateAndTimeFilterEntry), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Assertions.assertThrows(IllegalArgumentException.class, action);
    }

    @ParameterizedTest
    @ValueSource(strings = {"GreaterThanOrEqual", "LesserThanOrEqual"})
    public void ShouldProperlyMap_TimeFilter(String comparisonType){
        var timeFilterEntry = new TimeFilterEntry("registrationNumber", "whatever", "whatever", comparisonType);

        var result = FlightFiltersMapper.map(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), List.of(timeFilterEntry));

        Assertions.assertEquals(1, result.size());
        var filter = result.get(0);
        Assertions.assertEquals(filter.getClass(), FlightTimeFilter.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Equals", "Contains"})
    public void ShouldThrowException_WhenInvalidTimeFilter(String comparisonType){
        var timeFilterEntry = new TimeFilterEntry("registrationNumber", "whatever", "whatever", comparisonType);

        Executable action = () -> FlightFiltersMapper.map(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), List.of(timeFilterEntry));

        Assertions.assertThrows(IllegalArgumentException.class, action);
    }
}
