package com.example.backend.domain.flightRecord;

import org.springframework.stereotype.Service;

@Service
public class FlightRecordService {
    private final FlightRecordRepository flightRecordRepository;

    public FlightRecordService(FlightRecordRepository flightRecordRepository) {
        this.flightRecordRepository = flightRecordRepository;
    }

    public boolean isRecordRegister(String filename){
        var record = flightRecordRepository.findFirstByFilename(filename);
        return record.isPresent();
    }
}
