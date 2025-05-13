package com.example.backend.domain.flightRecord.dto;

import com.example.backend.domain.flightRecord.FlightRecordEntity;
import lombok.Data;


@Data
public class FlightRecordDto {
    private double latitude;
    private double longitude;
    private int heading;
    private int speed;
    private int altitude;
    private int fuel;

    private FlightRecordDto(double latitude, double longitude, int heading, int speed, int altitude, int fuel) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.heading = heading;
        this.speed = speed;
        this.altitude = altitude;
        this.fuel = fuel;
    }

    public static FlightRecordDto fromFlightRecordEntity(FlightRecordEntity flightRecordEntity) {
        return new FlightRecordDto(
                flightRecordEntity.getLatitude(),
                flightRecordEntity.getLongitude(),
                flightRecordEntity.getHeading(),
                flightRecordEntity.getSpeed(),
                flightRecordEntity.getAltitude(),
                flightRecordEntity.getFuel()
                );
    }
}
