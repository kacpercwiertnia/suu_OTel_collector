package com.example.backend.domain.drone;

import com.example.backend.events.recordRegistration.model.DroneRecordToRegister;
import com.example.backend.events.recordRegistration.model.envelope.RegistrationFlag;
import com.example.backend.domain.flightRecord.FlightRecordEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(indexes = {
        @Index(name = "model_idx", columnList = "model")
})
public class DroneEntity {
    @Id
    @Getter
    @Setter
    private String registrationNumber;
    @Getter
    @Setter
    private boolean isAirborne;
    @Getter
    @Setter
    private String country;
    @Getter
    @Setter
    private String operator;
    @Getter
    @Setter
    private int identification;
    @Getter
    @Setter
    private String identificationLabel;
    @Getter
    @Setter
    private String model;
    @Getter
    @Setter
    private String sign;
    @Getter
    @Setter
    private String type;
    @Getter
    @Setter
    private double recentLatitude;
    @Getter
    @Setter
    private double recentLongitude;
    @Getter
    @Setter
    private int recentHeading;
    @Getter
    @Setter
    private int recentSpeed;
    @Getter
    @Setter
    private int recentAltitude;
    @Getter
    @Setter
    private int recentFuel;

    @OneToMany
    @Getter
    @Setter
    private List<FlightRecordEntity> flightRecords;

    public DroneEntity(){}

    public DroneEntity(String registrationNumber,
                       boolean isAirborne, String country,
                       String operator, int identification,
                       String identificationLabel, String model, String sign,
                       String type, double latitude, double longitude, int heading,
                       int speed, int altitude, int fuel){
        this.registrationNumber = registrationNumber;
        this.isAirborne = isAirborne;
        this.country = country;
        this.operator = operator;
        this.identification = identification;
        this.identificationLabel = identificationLabel;
        this.model = model;
        this.sign = sign;
        this.type = type;
        this.recentLatitude = latitude;
        this.recentLongitude = longitude;
        this.recentHeading = heading;
        this.recentSpeed = speed;
        this.recentAltitude = altitude;
        this.recentFuel = fuel;
        this.flightRecords = new ArrayList<>();
    }

    public static DroneEntity fromDroneToRegister(DroneRecordToRegister drone){
        return new DroneEntity(drone.getRegistrationNumber(),
                RegistrationFlag.mapToAirborne(drone.getFlightRecord().getFlag()),
                drone.getCountry(),
                drone.getOperator(),
                drone.getIdentification().getValue(),
                drone.getIdentificationLabel(),
                drone.getModel(),
                drone.getSign(),
                RegistrationFlag.mapToType(drone.getFlightRecord().getFlag()),
                drone.getFlightRecord().getLatitude().getValue(),
                drone.getFlightRecord().getLongitude().getValue(),
                drone.getFlightRecord().getHeading().getValue(),
                drone.getFlightRecord().getSpeed(),
                drone.getFlightRecord().getAltitude(),
                drone.getFlightRecord().getFuel());
    }
}
