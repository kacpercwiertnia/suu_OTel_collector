package com.example.backend.unit.domain.drone.filtering;

import com.example.backend.common.filtering.ComparisonType;
import com.example.backend.domain.drone.filtering.DroneTextFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class DroneTextFiltersTests {

    @ParameterizedTest
    @ValueSource(strings = {"Equals", "Contains"})
    public void ShouldCreateTextFilter(String comparisonType){
        var result = new DroneTextFilter("attribute", "johnny", Enum.valueOf(ComparisonType.class, comparisonType));
    }

    @ParameterizedTest
    @ValueSource(strings = {"GreaterThanOrEqual", "LesserThanOrEqual"})
    public void ShouldThrowException_WhenInvalidComparisonType(String comparisonType){
        var enumValue = Enum.valueOf(ComparisonType.class, comparisonType);
        Executable action = () -> new DroneTextFilter("attribute", "johnny", enumValue);

        Assertions.assertThrows(IllegalArgumentException.class, action);
    }
}
