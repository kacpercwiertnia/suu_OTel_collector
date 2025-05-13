package com.example.backend;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import org.springframework.stereotype.Component;

@Component
public class ValidatedDroneRecordsCounter {
    private LongCounter counter;


    public ValidatedDroneRecordsCounter(OpenTelemetry openTelemetry){
        var meter = openTelemetry.getMeter("drones-files-processing");

        this.counter = meter.counterBuilder("validated.drones.records")
                .setDescription("Number of validated drone records received from simulator")
                .setUnit("number")
                .build();
    }

    public void logValid(int number){
        this.counter.add(number, getValidAttributes());
    }

    public void logInvalid(int number){
        this.counter.add(number, getInvalidAttributes());
    }

    private static Attributes getValidAttributes(){
        return Attributes.of(AttributeKey.stringKey("validation.result"), "valid");
    }

    private static Attributes getInvalidAttributes(){
        return Attributes.of(AttributeKey.stringKey("validation.result"), "invalid");
    }
}
