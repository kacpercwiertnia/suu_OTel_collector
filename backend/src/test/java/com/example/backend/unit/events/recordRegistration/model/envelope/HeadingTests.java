package com.example.backend.unit.events.recordRegistration.model.envelope;

import com.example.backend.events.recordRegistration.model.envelope.Heading;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class HeadingTests {

    @Test
    public void ShouldReturnHeading_GivenValueBetweenZeroAnd360(){
        var result = new Heading(69);
        Assertions.assertEquals(result.getValue(), 69);
    }

    @ParameterizedTest
    @ValueSource(ints = {-69, 420})
    public void ShouldThrowIllegalArgumentException_GivenValueAbove360OrBelowZero(int value){
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Heading(value),
                "Heading value must fit between 0-360");
    }

}
