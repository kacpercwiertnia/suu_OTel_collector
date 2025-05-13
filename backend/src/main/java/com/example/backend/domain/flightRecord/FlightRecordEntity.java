package com.example.backend.domain.flightRecord;

import com.example.backend.domain.drone.DroneEntity;
import com.example.backend.domain.flight.FlightEntity;
import com.example.backend.events.recordRegistration.model.FlightRecordToRegister;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(indexes = {
        @Index(name = "date_time_index", columnList = "DATE, TIME"),
        @Index(name = "altitude_index", columnList = "ALTITUDE"),
        @Index(name = "longitude_index", columnList = "LONGITUDE"),
        @Index(name = "latitude_index", columnList = "LATITUDE"),
        @Index(name = "fuel_index", columnList = "FUEL"),
        @Index(name = "drone_index", columnList = "DRONE_REGISTRATION_NUMBER")
})
public class FlightRecordEntity {
    @Id
    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private String filename;
    @Getter
    @Setter
    private String server;
    @Getter
    @Setter
    @Column(name = "DATE")
    private LocalDate date;
    @Getter
    @Setter
    @Column(name = "TIME")
    private LocalTime time;
    @Getter
    @Setter
    private String flag;
    @Getter
    @Setter
    private String systemId;
    @Getter
    @Setter
    private double latitude;
    @Getter
    @Setter
    private double longitude;
    @Getter
    @Setter
    private int heading;
    @Getter
    @Setter
    private int speed;
    @Getter
    @Setter
    private int altitude;
    @Getter
    @Setter
    private int fuel;

    @ManyToOne
    @Getter
    @Setter
    private DroneEntity drone;

    @ManyToOne
    @Getter
    @Setter
    private FlightEntity flight;
    public FlightRecordEntity(){}

    public FlightRecordEntity(String id, String filename, String server, LocalDate date, LocalTime time, String flag, String systemId,
                              double latitude, double longitude, int heading, int speed, int altitude, int fuel) {
        this.id = id;
        this.filename = filename;
        this.server = server;
        this.date = date;
        this.time = time;
        this.flag = flag;
        this.systemId = systemId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.heading = heading;
        this.speed = speed;
        this.altitude = altitude;
        this.fuel = fuel;
    }

    public static FlightRecordEntity fromFlightRecordToRegister(FlightRecordToRegister flightRecord){
        return new FlightRecordEntity(
            flightRecord.getId(),
            flightRecord.getFilename(),
            flightRecord.getServer(),
            flightRecord.getDate(),
            flightRecord.getTime(),
            flightRecord.getFlag().toString(),
            flightRecord.getSystemId(),
            flightRecord.getLatitude().getValue(),
            flightRecord.getLongitude().getValue(),
            flightRecord.getHeading().getValue(),
            flightRecord.getSpeed(),
            flightRecord.getAltitude(),
            flightRecord.getFuel()
        );
    }
}
