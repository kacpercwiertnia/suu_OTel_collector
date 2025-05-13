package com.example.backend.events.recordRegistration.model;

import com.example.backend.events.recordRegistration.model.envelope.Identification;
import com.example.backend.simulatorIntegration.model.DroneFromSimulator;
import lombok.Data;

@Data
public class DroneRecordToRegister {

    private final String country;
    private final String operator;
    private final Identification identification;
    private final String identificationLabel;
    private final String model;
    private final String registrationNumber;
    private final String sign;
    private final String type;
    private final FlightRecordToRegister flightRecord;

    private DroneRecordToRegister(String country, String operator, Identification identification, String identificationLabel,
                                  String model, String registrationNumber, String sign, String type, FlightRecordToRegister flightRecord){
        this.country = country;
        this.operator = operator;
        this.identification = identification;
        this.identificationLabel = identificationLabel;
        this.model = model;
        this.registrationNumber = registrationNumber;
        this.sign = sign;
        this.type = type;
        this.flightRecord = flightRecord;
    }

    public static DroneRecordToRegister fromDroneFromSimulator(DroneFromSimulator droneFromSimulator){
        return new DroneRecordToRegister(droneFromSimulator.getCountry(),
                droneFromSimulator.getOperator(),
                new Identification(droneFromSimulator.getIdentification()),
                droneFromSimulator.getIdentificationLabel(),
                droneFromSimulator.getModel(),
                droneFromSimulator.getRegistrationNumber(),
                droneFromSimulator.getSign(),
                droneFromSimulator.getType(),
                FlightRecordToRegister.fromDroneFromSimulator(droneFromSimulator));
    }
}