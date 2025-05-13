package com.example.backend.domain.flight.dto;

import com.example.backend.domain.drone.dto.PositionDto;
import com.example.backend.domain.flight.FlightEntity;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class FlightSummaryDto {
    private Long id;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private LocalTime duration;
    private double averageSpeed;
    private int elevationGain;
    private double distance;
    private boolean didLand;
    private List<PositionDto> flightRecords;

    private FlightSummaryDto(Long id, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, LocalTime duration, double averageSpeed, int elevationGain, double distance, boolean didLand, List<PositionDto> flightRecords) {
        this.id = id;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.duration = duration;
        this.averageSpeed = averageSpeed;
        this.elevationGain = elevationGain;
        this.distance = distance;
        this.didLand = didLand;
        this.flightRecords = flightRecords;
    }

    public static FlightSummaryDto fromFlightEntity(FlightEntity flightEntity) {
        return new FlightSummaryDto(
                flightEntity.getId(),
                flightEntity.getStartDate(),
                flightEntity.getStartTime(),
                flightEntity.getEndDate(),
                flightEntity.getEndTime(),
                flightEntity.getDuration(),
                flightEntity.getAverageSpeed(),
                flightEntity.getElevationGain(),
                flightEntity.getDistance(),
                flightEntity.isDidLand(),
                flightEntity.getFlightRecords().stream().map(record -> new PositionDto(record.getLatitude(), record.getLongitude(), record.getAltitude())).toList());
    }
}
