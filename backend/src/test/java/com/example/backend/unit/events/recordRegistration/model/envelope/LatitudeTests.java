package com.example.backend.unit.events.recordRegistration.model.envelope;

import com.example.backend.events.recordRegistration.model.envelope.Latitude;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class LatitudeTests {
    
    @ParameterizedTest
    @CsvSource({
            "213763N, 21.634166666666665",
            "312423S, -31.406388888888888"
    })
    public void ShouldReturnCorrectLatitude_GivenCorrectArgument(String latToParse, double expectedRes){
        var result = new Latitude(latToParse);
        Assertions.assertEquals(result.getValue(), expectedRes);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234N", "123123123N"})
    public void ShouldThrowIllegalArgumentException_GivenArgumentOfLenghtOtherThanSeven(String Latitude){
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Latitude(Latitude)
                ,"Latitude length must be 6 characters");
    }

    @Test
    public void ShouldThrowNumberFormatException_WhenFirstPartIsNotFullOfDigits(){
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Latitude("XDDDDN"));
    }

    @Test
    public void ShouldThrowIllegalArgumentException_WhenDirectionIsNotNorS(){
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Latitude("123123E")
                ,"Latitude should end with N or S character");
    }


}
