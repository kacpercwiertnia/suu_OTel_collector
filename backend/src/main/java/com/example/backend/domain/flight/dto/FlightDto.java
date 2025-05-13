package com.example.backend.domain.flight.dto;

import com.example.backend.domain.flight.FlightEntity;
import com.example.backend.domain.flightRecord.dto.FlightRecordDto;
import lombok.Data;

import java.util.List;

@Data
public class FlightDto {
    private Long id;
    private List<FlightRecordDto> flightRecords;

    private FlightDto(Long id, List<FlightRecordDto> flightRecords) {
        this.id = id;
        this.flightRecords = flightRecords;
    }

    public static FlightDto fromFlightEntity(FlightEntity flightEntity) {
        return new FlightDto(
                flightEntity.getId(),
                flightEntity.getFlightRecords().stream().map(FlightRecordDto::fromFlightRecordEntity).toList());
    }
}
