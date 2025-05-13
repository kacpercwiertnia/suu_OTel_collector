package com.example.backend.unit.events.mediator;

import com.example.backend.events.mediator.ICommandHandler;
import com.example.backend.events.mediator.Mediator;
import com.example.backend.events.deadDronesStoppage.commands.StopDeadDronesCommand;
import com.example.backend.events.deadDronesStoppage.handlers.StopDeadDronesCommandHandler;
import com.example.backend.events.recordRegistration.handlers.SaveRecordsCommandHandler;
import com.example.backend.unit.simulatorIntegration.model.DroneFromSimulatorFixtureBuilder;
import com.example.backend.events.recordRegistration.commands.SaveRecordsCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

public class MediatorTests {
    private Mediator sut;
    private ICommandHandler<SaveRecordsCommand> saveHandler;
    private ICommandHandler<StopDeadDronesCommand> checkHandler;

    @BeforeEach
    public void setUp(){
        saveHandler = Mockito.mock(SaveRecordsCommandHandler.class);
        checkHandler = Mockito.mock(StopDeadDronesCommandHandler.class);
        sut = new Mediator(saveHandler, checkHandler);
    }

    @Test
    public void ShouldCallSaveRecordsCommandHandler_WhenReceivesSaveRecordsCommand(){
        var drone = new DroneFromSimulatorFixtureBuilder().build();
        var dronesFromSimulator = List.of(drone);
        SaveRecordsCommand saveCommand = new SaveRecordsCommand(dronesFromSimulator);
        StopDeadDronesCommand checkCommand = new StopDeadDronesCommand(dronesFromSimulator);

        sut.send(saveCommand);
        sut.send(checkCommand);

        Mockito.verify(saveHandler).handle(saveCommand);
        Mockito.verify(checkHandler).handle(checkCommand);
    }
}
