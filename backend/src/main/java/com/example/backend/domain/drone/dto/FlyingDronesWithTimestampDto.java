package com.example.backend.domain.drone.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record FlyingDronesWithTimestampDto(List<FlyingDroneDto> flyingDrones, LocalDate date, LocalTime time) {
}