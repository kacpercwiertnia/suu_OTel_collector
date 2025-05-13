package com.example.backend.events.recordRegistration.model.envelope;

import lombok.Getter;

import java.util.Objects;

public class Longitude {
    @Getter
    private double value;

    public Longitude(String value) throws IllegalArgumentException{
        var numericPart = value.substring(0, value.length()-1);
        var parsedNumericPart = parseLongitude(numericPart);
        this.value = adjustCoordinate(parsedNumericPart, value.charAt(value.length() - 1));
    }

    private double parseLongitude(String longitude){
        if (longitude.length() != 7) throw new IllegalArgumentException("longitude length must be 7 characters");

        int degrees = Integer.parseInt(longitude.substring(0, 3));
        int minutes = Integer.parseInt(longitude.substring(3, 5));
        int seconds = Integer.parseInt(longitude.substring(5, 7));

        return degrees + (double) minutes /60 + (double) seconds /3600;
    }

    private double adjustCoordinate(double coordinate, char direction){
        return switch (direction){
            case 'E' -> coordinate;
            case 'W' -> coordinate*-1;
            default -> throw new IllegalArgumentException("longitude should end with E or W character");
        };
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Longitude longitude = (Longitude) obj;
        return Objects.equals(value, longitude.value);
    }

    @Override
    public int hashCode(){
        return Objects.hash(value);
    }
}
