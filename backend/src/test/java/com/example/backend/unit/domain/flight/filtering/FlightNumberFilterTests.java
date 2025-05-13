package com.example.backend.unit.domain.flight.filtering;

import com.example.backend.common.filtering.ComparisonType;
import com.example.backend.domain.flight.filtering.FlightNumberFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class FlightNumberFilterTests {
    @ParameterizedTest
    @ValueSource(strings = {"Equals", "GreaterThanOrEqual", "LesserThanOrEqual"})
    public void ShouldCreateNumberFilter(String comparisonType){
        var result = new FlightNumberFilter("attribute", 50, Enum.valueOf(ComparisonType.class, comparisonType));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Contains"})
    public void ShouldThrowException_WhenInvalidComparisonType(String comparisonType){
        var enumValue = Enum.valueOf(ComparisonType.class, comparisonType);
        Executable action = () -> new FlightNumberFilter("attribute", 50, enumValue);

        Assertions.assertThrows(IllegalArgumentException.class, action);
    }
}
