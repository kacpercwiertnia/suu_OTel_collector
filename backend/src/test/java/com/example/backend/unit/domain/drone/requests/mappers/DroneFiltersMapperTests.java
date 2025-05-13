package com.example.backend.unit.domain.drone.requests.mappers;

import com.example.backend.domain.drone.filtering.IDroneFilter;
import com.example.backend.domain.drone.filtering.DroneNumberFilter;
import com.example.backend.domain.drone.filtering.DroneTextFilter;
import com.example.backend.domain.drone.requests.mappers.DroneFiltersMapper;
import com.example.backend.common.filtering.dtos.NumberFilterEntry;
import com.example.backend.common.filtering.dtos.TextFilterEntry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

public class DroneFiltersMapperTests {

    @ParameterizedTest
    @ValueSource(strings = {"Equals", "Contains"})
    public void ShouldProperlyMap_TextFilter(){
        var textFilterEntry = new TextFilterEntry("registrationNumber", "whatever", "Johnny", "Equals");

        List<IDroneFilter> result = DroneFiltersMapper.map(List.of(textFilterEntry), new ArrayList<>());

        Assertions.assertEquals(1, result.size());
        var filter = result.get(0);
        Assertions.assertEquals(filter.getClass(), DroneTextFilter.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"GreaterThanOrEqual", "LesserThanOrEqual", "ZlyEnum"})
    public void ShouldThrowException_WhenInvalidTextFilter(String comparisonType){
        var textFilterEntry = new TextFilterEntry("registrationNumber", "whatever", "Johnny", comparisonType);

        Executable action = () -> DroneFiltersMapper.map(List.of(textFilterEntry), new ArrayList<>());

        Assertions.assertThrows(IllegalArgumentException.class, action);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Equals", "GreaterThanOrEqual", "LesserThanOrEqual"})
    public void ShouldProperlyMap_NumberFilter(String comparisonType){
        var numberFilterEntry = new NumberFilterEntry("registrationNumber", "whatever", 69, comparisonType);

        var result = DroneFiltersMapper.map(new ArrayList<>(), List.of(numberFilterEntry));

        Assertions.assertEquals(1, result.size());
        var filter = result.get(0);
        Assertions.assertEquals(filter.getClass(), DroneNumberFilter.class);
    }



    @ParameterizedTest
    @ValueSource(strings = {"ZlyEnum", "Contains"})
    public void ShouldThrowException_WhenInvalidNumberFilter(String comparisonType){
        var numberFilterEntry = new NumberFilterEntry("registrationNumber", "whatever", 69, comparisonType);

        Executable action = () -> DroneFiltersMapper.map(new ArrayList<>(), List.of(numberFilterEntry));

        Assertions.assertThrows(IllegalArgumentException.class, action);
    }
}
