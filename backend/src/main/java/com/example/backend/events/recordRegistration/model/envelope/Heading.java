package com.example.backend.events.recordRegistration.model.envelope;

import lombok.Getter;

import java.util.Objects;

public class Heading {
    @Getter
    private int value;

    public Heading(int value) throws IllegalArgumentException{
        if (value < 0 || value > 360) throw new IllegalArgumentException("Heading value must fit between 0-360");
        this.value = value;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Heading heading = (Heading) obj;
        return Objects.equals(value, heading.value);
    }

    @Override
    public int hashCode(){
        return Objects.hash(value);
    }
}
