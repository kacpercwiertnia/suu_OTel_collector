package com.example.backend.events.recordRegistration.model.envelope;

import lombok.Getter;

import java.util.Objects;

public class Identification {
    @Getter
    private int value;

    public Identification(int value) throws IllegalArgumentException{
        if (value < 1 || value > 16) throw new IllegalArgumentException("Not acceptable value for Identification");
        this.value = value;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Identification identification = (Identification) obj;
        return Objects.equals(value, identification.value);
    }

    @Override
    public int hashCode(){
        return Objects.hash(value);
    }
}
