package com.example.backend.unit.common.filtering;

import com.example.backend.common.filtering.ComparisonType;
import com.example.backend.common.filtering.FilterType;
import com.example.backend.common.filtering.ComparisonTypeForFilterValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class ComparisonTypeForFilterValidatorTests {
    @ParameterizedTest
    @EnumSource(value = ComparisonType.class, names = {"Equals"})
    public void ShouldReturnTrue_ForValidComparisonType_ForTextFilter(ComparisonType type){
        var result = ComparisonTypeForFilterValidator.isValid(type, FilterType.Text);
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @EnumSource(value = ComparisonType.class, names = {"Equals", "GreaterThanOrEqual", "LesserThanOrEqual"})
    public void ShouldReturnTrue_ForValidComparisonType_ForNumberFilter(ComparisonType type){
        var result = ComparisonTypeForFilterValidator.isValid(type, FilterType.Number);
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @EnumSource(value = ComparisonType.class, names = {"Contains"})
    public void ShouldReturnFalse_ForInvalidComparisonType_ForTextFilter(ComparisonType type){
        var result = ComparisonTypeForFilterValidator.isValid(type, FilterType.Number);
        Assertions.assertFalse(result);
    }

    @ParameterizedTest
    @EnumSource(value = ComparisonType.class, names = {"GreaterThanOrEqual", "LesserThanOrEqual"})
    public void ShouldReturnTrue_ForValidComparisonType_ForDateAndTimeFilter(ComparisonType type){
        var result = ComparisonTypeForFilterValidator.isValid(type, FilterType.DateAndTime);
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @EnumSource(value = ComparisonType.class, names = {"Contains", "Equals"})
    public void ShouldReturnFalse_ForInvalidComparisonType_ForDateAndTimeFilter(ComparisonType type){
        var result = ComparisonTypeForFilterValidator.isValid(type, FilterType.DateAndTime);
        Assertions.assertFalse(result);
    }

    @ParameterizedTest
    @EnumSource(value = ComparisonType.class, names = {"GreaterThanOrEqual", "LesserThanOrEqual"})
    public void ShouldReturnTrue_ForValidComparisonType_ForTimeFilter(ComparisonType type){
        var result = ComparisonTypeForFilterValidator.isValid(type, FilterType.Time);
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @EnumSource(value = ComparisonType.class, names = {"Contains", "Equals"})
    public void ShouldReturnFalse_ForInvalidComparisonType_ForTimeFilter(ComparisonType type){
        var result = ComparisonTypeForFilterValidator.isValid(type, FilterType.Time);
        Assertions.assertFalse(result);
    }

    @ParameterizedTest
    @EnumSource(value = ComparisonType.class, names = {"Contains", "GreaterThanOrEqual", "LesserThanOrEqual"})
    public void ShouldReturnFalse_ForInvalidComparisonType_ForBooleanFilter(ComparisonType type){
        var result = ComparisonTypeForFilterValidator.isValid(type, FilterType.Boolean);
        Assertions.assertFalse(result);
    }

    @ParameterizedTest
    @EnumSource(value = ComparisonType.class, names = {"Equals"})
    public void ShouldReturnTrue_ForValidComparisonType_ForBooleanFilter(ComparisonType type){
        var result = ComparisonTypeForFilterValidator.isValid(type, FilterType.Boolean);
        Assertions.assertTrue(result);
    }
}
