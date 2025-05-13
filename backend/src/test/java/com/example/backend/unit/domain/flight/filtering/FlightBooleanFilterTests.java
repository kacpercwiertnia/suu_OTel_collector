package com.example.backend.unit.domain.flight.filtering;

import com.example.backend.common.filtering.ComparisonType;
import com.example.backend.domain.flight.filtering.FlightBooleanFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class FlightBooleanFilterTests {
    @ParameterizedTest
    @ValueSource(strings = {"Equals"})
    public void ShouldCreateNumberFilter(String comparisonType){
        var result = new FlightBooleanFilter("attribute", true, Enum.valueOf(ComparisonType.class, comparisonType));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Contains", "GreaterThanOrEqual", "LesserThanOrEqual"})
    public void ShouldThrowException_WhenInvalidComparisonType(String comparisonType){
        var enumValue = Enum.valueOf(ComparisonType.class, comparisonType);
        Executable action = () -> new FlightBooleanFilter("attribute", true, enumValue);

        Assertions.assertThrows(IllegalArgumentException.class, action);
    }
}
