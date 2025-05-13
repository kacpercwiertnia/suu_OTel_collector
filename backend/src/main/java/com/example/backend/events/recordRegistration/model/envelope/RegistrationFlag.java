package com.example.backend.events.recordRegistration.model.envelope;

public enum RegistrationFlag {
    BEG,
    UPD,
    DROP,
    EXT;

    public static boolean mapToAirborne(RegistrationFlag flag){
        return switch (flag){
            case BEG, UPD, EXT -> true;
            case DROP  -> false;
        };
    }

    public static String mapToType(RegistrationFlag flag){
        return switch (flag){
            case BEG, UPD, EXT -> "Airborne";
            case DROP  -> "Grounded";
        };
    }
}
