package com.example.backend.unit.events.recordRegistration.model.envelope;

import com.example.backend.events.recordRegistration.model.envelope.RegistrationFlag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

public class RegistrationFlagTests {

    @ParameterizedTest
    @MethodSource("provideAirbourneEnums")
    public void mapToIsAirbourneShouldReturnTrue_WhenGivenOtherFlagThanDrop(RegistrationFlag flag){
        boolean result = RegistrationFlag.mapToAirborne(flag);
        Assertions.assertTrue(result);
    }

    @Test
    public void mapToIsAirbourneShouldReturnFalse_WhenGivenRegistrationFlagOfDrop(){
        boolean result = RegistrationFlag.mapToAirborne(RegistrationFlag.DROP);
        Assertions.assertFalse(result);
    }

    static Stream<Arguments> provideAirbourneEnums(){
        return Stream.of(
                Arguments.of(RegistrationFlag.BEG),
                Arguments.of(RegistrationFlag.UPD),
                Arguments.of(RegistrationFlag.EXT)
        );
    }
}
