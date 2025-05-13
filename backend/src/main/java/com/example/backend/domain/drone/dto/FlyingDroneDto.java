package com.example.backend.domain.drone.dto;

import com.example.backend.domain.drone.DroneEntity;
import lombok.Data;

import java.util.List;

@Data
public class FlyingDroneDto {
    private final String registrationNumber;
    private final String country;
    private final String operator;
    private final int identification;
    private final String model;
    private final String sign;
    private final String type;
    private final int heading;
    private final int speed;
    private final int fuel;
    private final PositionDto currentPosition;
    private final List<PositionDto> trace;

    private FlyingDroneDto(String registrationNumber, String country, String operator, int identification, String model,
                           String sign, String type, int heading, int speed, int fuel,
                           PositionDto currentPosition, List<PositionDto> trace) {
        this.registrationNumber = registrationNumber;
        this.country = country;
        this.operator = operator;
        this.identification = identification;
        this.model = model;
        this.sign = sign;
        this.type = type;
        this.heading = heading;
        this.speed = speed;
        this.fuel = fuel;
        this.currentPosition = currentPosition;
        this.trace = trace;
    }

    public static FlyingDroneDto fromDroneEntity(DroneEntity entity){
        var positions = entity.getFlightRecords();
        var trace = positions
                .subList(1 , positions.size())
                .stream().map(position -> new PositionDto(position.getLatitude(),
                        position.getLongitude(),
                        position.getAltitude()))
                .toList();

        return new FlyingDroneDto(
                entity.getRegistrationNumber(),
                entity.getCountry(),
                entity.getOperator(),
                entity.getIdentification(),
                entity.getModel(),
                entity.getSign(),
                entity.getType(),
                entity.getRecentHeading(),
                entity.getRecentSpeed(),
                entity.getRecentFuel(),
                new PositionDto(entity.getRecentLatitude(), entity.getRecentLongitude(),
                        entity.getRecentAltitude()),
                trace
        );
    }
}
