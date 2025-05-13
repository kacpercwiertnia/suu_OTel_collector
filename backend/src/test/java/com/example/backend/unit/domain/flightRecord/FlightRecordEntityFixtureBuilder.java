package com.example.backend.unit.domain.flightRecord;

import com.example.backend.domain.flight.FlightEntity;
import com.example.backend.domain.flightRecord.FlightRecordEntity;

import java.time.LocalDate;
import java.time.LocalTime;

public class FlightRecordEntityFixtureBuilder {
    private FlightRecordEntity flightRecord = getDefaultRecord();

    private FlightRecordEntity getDefaultRecord() {
        return new FlightRecordEntity(
                "1",
                "filename",
                "server",
                LocalDate.now(),
                LocalTime.now(),
                "UPD",
                "systemId",
                69.69,
                69.69,
                69,
                123,
                69,
                69
        );
    }

    public FlightRecordEntityFixtureBuilder withId(String id){
        flightRecord.setId(id);
        return this;
    }

    public FlightRecordEntityFixtureBuilder withDateAndTime(LocalDate date, LocalTime time){
        flightRecord.setDate(date);
        flightRecord.setTime(time);
        return this;
    }

    public FlightRecordEntityFixtureBuilder withAltitude(int altitude){
        flightRecord.setAltitude(altitude);
        return this;
    }

    public FlightRecordEntityFixtureBuilder withLatitude(double latitude){
        flightRecord.setLatitude(latitude);
        return this;
    }

    public FlightRecordEntityFixtureBuilder withLongitude(double longitude){
        flightRecord.setLongitude(longitude);
        return this;
    }

    public FlightRecordEntityFixtureBuilder withFuel(int fuel){
        flightRecord.setFuel(fuel);
        return this;
    }

    public FlightRecordEntityFixtureBuilder withFlight(FlightEntity flight){
        flightRecord.setFlight(flight);
        return this;
    }

    public FlightRecordEntityFixtureBuilder withFilename(String filename){
        flightRecord.setFilename(filename);
        return this;
    }


    public FlightRecordEntity build(){
        return flightRecord;
    }
}
