package com.example.backend.unit.simulatorIntegration.model;

import com.example.backend.simulatorIntegration.model.DroneFromSimulator;

import java.time.LocalDate;
import java.time.LocalTime;

public class DroneFromSimulatorFixtureBuilder {
    private DroneFromSimulator drone = new DroneFromSimulator(
            "filename",
            "server",
            LocalDate.now(),
            LocalTime.now(),
            "DROP",
            "id",
            "idExt",
            "500423N",
            "1195723E",
            90,
            100,
            50,
            "Nigeria",
            "operator",
            7,
            "Magenta",
            "Belmondo",
            "420-69-2137",
            "sign",
            "type",
            69,
            "signal",
            "frequency",
            "sensorLat",
            "sensorLon",
            "sensorLabel"
    );

    public DroneFromSimulator build(){
        return drone;
    }

    public DroneFromSimulatorFixtureBuilder withLatitude(String lat){
        drone.setLatitude(lat);
        return this;
    }

    public DroneFromSimulatorFixtureBuilder withLongitude(String lon){
        drone.setLongitude(lon);
        return this;
    }

    public DroneFromSimulatorFixtureBuilder withHeading(int heading){
        drone.setHeading(heading);
        return this;
    }

    public DroneFromSimulatorFixtureBuilder withIdentification(int identification){
        drone.setIdentification(identification);
        return this;
    }

    public DroneFromSimulatorFixtureBuilder withFlag(String flag){
        drone.setFlag(flag);
        return this;
    }

    public DroneFromSimulatorFixtureBuilder withRegistrationNumber(String regNumber){
        drone.setRegistrationNumber(regNumber);
        return this;
    }

    public DroneFromSimulatorFixtureBuilder withFilename(String filename){
        drone.setFilename(filename);
        return this;
    }
}
