package com.example.backend.events.recordRegistration.model;

import com.example.backend.events.recordRegistration.model.envelope.RegistrationFlag;
import com.example.backend.events.recordRegistration.model.envelope.Heading;
import com.example.backend.events.recordRegistration.model.envelope.Latitude;
import com.example.backend.events.recordRegistration.model.envelope.Longitude;
import com.example.backend.simulatorIntegration.model.DroneFromSimulator;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class FlightRecordToRegister {
    private String filename;
    private String server;
    private LocalTime time;
    private LocalDate date;
    private RegistrationFlag flag;
    private String systemId;
    private String id;
    private Latitude latitude;
    private Longitude longitude;
    private int altitude;
    private Heading heading;
    private int speed;
    private int fuel;

    private FlightRecordToRegister(String filename,
                                   String server,
                                   LocalTime time,
                                   LocalDate date,
                                   RegistrationFlag flag,
                                   String systemId,
                                   String id,
                                   Latitude latitude,
                                   Longitude longitude,
                                   int altitude,
                                   Heading heading,
                                   int speed,
                                   int fuel) {
        this.filename = filename;
        this.server = server;
        this.time = time;
        this.date = date;
        this.flag = flag;
        this.systemId = systemId;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.heading = heading;
        this.speed = speed;
        this.fuel = fuel;
    }

    public static FlightRecordToRegister fromDroneFromSimulator(DroneFromSimulator drone){
        return new FlightRecordToRegister(drone.getFilename(),
                drone.getServer(),
                drone.getTime(),
                drone.getDate(),
                RegistrationFlag.valueOf(drone.getFlag()),
                drone.getId(),
                drone.getIdExt(),
                new Latitude(drone.getLatitude()),
                new Longitude(drone.getLongitude()),
                drone.getAltitude(),
                new Heading(drone.getHeading()),
                drone.getSpeed(),
                drone.getFuel()
                );
    }

}
