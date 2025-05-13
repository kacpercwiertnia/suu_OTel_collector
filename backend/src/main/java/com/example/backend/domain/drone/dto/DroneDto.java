package com.example.backend.domain.drone.dto;

import com.example.backend.domain.drone.DroneEntity;
import lombok.Data;

@Data
public class DroneDto {
    private final String registrationNumber;
    private final String country;
    private final String operator;
    private final int identification;
    private final String model;
    private final String sign;
    private final String type;

    private DroneDto(String registrationNumber, String country, String operator, int identification, String model,
                     String sign, String type){
        this.registrationNumber = registrationNumber;
        this.country = country;
        this.operator = operator;
        this.identification = identification;
        this.model = model;
        this.sign = sign;
        this.type = type;
    }

    public static DroneDto fromDroneEntity(DroneEntity entity){
        return new DroneDto(
            entity.getRegistrationNumber(),
            entity.getCountry(),
            entity.getOperator(),
            entity.getIdentification(),
            entity.getModel(),
            entity.getSign(),
            entity.getType());
    }
}
