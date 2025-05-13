package com.example.backend.simulatorIntegration;

import com.example.backend.events.mediator.IMediator;
import com.example.backend.events.deadDronesStoppage.commands.StopDeadDronesCommand;
import com.example.backend.events.recordRegistration.commands.SaveRecordsCommand;
import com.example.backend.simulatorIntegration.model.DroneFromSimulator;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@Slf4j
public class SimulatorIntegrationService {
    private final FilePathProvider filePathProvider;
    private final IMediator mediator;
    private final DroneRecordsCsvReader droneRecordsCsvReader;

    public SimulatorIntegrationService(FilePathProvider filePathProvider, IMediator mediator, DroneRecordsCsvReader droneRecordsCsvReader) {
        this.filePathProvider = filePathProvider;
        this.mediator = mediator;
        this.droneRecordsCsvReader = droneRecordsCsvReader;
    }

    public void ProcessIncomingFile(String filename){
        String path = filePathProvider.getFilePath(filename);
        List<DroneFromSimulator> parsedDrones = droneRecordsCsvReader.parseCsv(path);
        mediator.send(new SaveRecordsCommand(parsedDrones));
        mediator.send(new StopDeadDronesCommand(parsedDrones));
        removeFile(path);
    }

    private void removeFile(String path){
        File file = new File(path);
        if (file.delete())
            log.info("Deleted file: " + path);
        else
            log.error("Failed to delete file: " + path);
    }
}
