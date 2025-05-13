package com.example.backend.domain.drone.mappers;

import com.example.backend.domain.drone.DroneEntity;
import com.example.backend.domain.flightRecord.FlightRecordEntity;

public record DroneEntityWithFlightRecordEntity(DroneEntity drone, FlightRecordEntity flightRecord){}