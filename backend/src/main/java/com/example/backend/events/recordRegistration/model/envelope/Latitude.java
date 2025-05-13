package com.example.backend.events.recordRegistration.model.envelope;

import lombok.Getter;

import java.util.Objects;

public class Latitude {
    @Getter
    private double value;

    public Latitude(String value) throws IllegalArgumentException{
        var numericPart = value.substring(0, value.length()-1);
        var parsedNumericPart = parseLatitude(numericPart);
        this.value = adjustCoordinate(parsedNumericPart, value.charAt(value.length() - 1));

    }

    private double parseLatitude(String latitude){
        if (latitude.length() != 6) throw new IllegalArgumentException("Latitude lenght must be 6 characters");

        int degrees = Integer.parseInt(latitude.substring(0, 2));
        int minutes = Integer.parseInt(latitude.substring(2, 4));
        int seconds = Integer.parseInt(latitude.substring(4, 6));

        return degrees + (double) minutes /60 + (double) seconds /3600;
    }

    private double adjustCoordinate(double coordinate, char direction){
        return switch (direction){
            case 'N' -> coordinate;
            case 'S' -> coordinate*-1;
            default -> throw new IllegalArgumentException("Latitude should end with N or S character");
        };
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Latitude latitude = (Latitude) obj;
        return Objects.equals(value, latitude.value);
    }

    @Override
    public int hashCode(){
        return Objects.hash(value);
    }
}
