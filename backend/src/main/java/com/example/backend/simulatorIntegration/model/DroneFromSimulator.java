package com.example.backend.simulatorIntegration.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class DroneFromSimulator {
    //general
    @CsvBindByName(column = "Filename")
    private String filename;

    @CsvBindByName(column = "Server")
    private String server;

    @CsvBindByName(column = "Date")
    @CsvDate("ddMMyyyy")
    private LocalDate date;

    @CsvBindByName(column = "Time")
    @CsvDate("HH:mm:ss.SSSS")
    private LocalTime time;

    @CsvBindByName(column = "Flag")
    private String flag;

    @CsvBindByName(column = "Id")
    private String id;

    @CsvBindByName(column = "IdExt")
    private String idExt;

    //flight parameters
    @CsvBindByName(column = "Latitude")
    private String latitude;

    @CsvBindByName(column = "Longitude")
    private String longitude;

    @CsvBindByName(column = "Heading")
    private int heading;

    @CsvBindByName(column = "Speed")
    private int speed;

    @CsvBindByName(column = "Altitude")
    private int altitude;

    // drone
    @CsvBindByName(column = "Country")
    private String country;

    @CsvBindByName(column = "Operator")
    private String operator;

    @CsvBindByName(column = "Identification")
    private int identification;

    @CsvBindByName(column = "IdentificationLabel")
    private String IdentificationLabel;

    @CsvBindByName(column = "Model")
    private String model;

    @CsvBindByName(column = "RegistrationNumber")
    private String registrationNumber;

    @CsvBindByName(column = "Sign")
    private String sign;

    @CsvBindByName(column = "Type")
    private String type;

    @CsvBindByName(column = "Fuel")
    private int fuel;

    // sensor
    @CsvBindByName(column = "Signal")
    private String signal;

    @CsvBindByName(column = "Frequency")
    private String frequency;

    @CsvBindByName(column = "SensorLat")
    private String sensorLat;

    @CsvBindByName(column = "SensorLon")
    private String sensorLon;

    @CsvBindByName(column = "SensorLabel")
    private String sensorLabel;

    public DroneFromSimulator(String filename,
                              String server,
                              LocalDate date,
                              LocalTime time,
                              String flag,
                              String id,
                              String idExt,
                              String latitude,
                              String longitude,
                              int heading,
                              int speed,
                              int altitude,
                              String country,
                              String operator,
                              int identification,
                              String identificationLabel,
                              String model,
                              String registrationNumber,
                              String sign,
                              String type,
                              int fuel,
                              String signal,
                              String frequency,
                              String sensorLat,
                              String sensorLon,
                              String sensorLabel) {
        this.filename = filename;
        this.server = server;
        this.date = date;
        this.time = time;
        this.flag = flag;
        this.id = id;
        this.idExt = idExt;
        this.latitude = latitude;
        this.longitude = longitude;
        this.heading = heading;
        this.speed = speed;
        this.altitude = altitude;
        this.country = country;
        this.operator = operator;
        this.identification = identification;
        this.IdentificationLabel = identificationLabel;
        this.model = model;
        this.registrationNumber = registrationNumber;
        this.sign = sign;
        this.type = type;
        this.fuel = fuel;
        this.signal = signal;
        this.frequency = frequency;
        this.sensorLat = sensorLat;
        this.sensorLon = sensorLon;
        this.sensorLabel = sensorLabel;
    }

    public DroneFromSimulator(){}
}
