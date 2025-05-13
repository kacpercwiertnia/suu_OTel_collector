package com.example.backend.events.mediator;

import com.example.backend.events.deadDronesStoppage.commands.StopDeadDronesCommand;
import com.example.backend.events.recordRegistration.commands.SaveRecordsCommand;
import org.springframework.stereotype.Component;

@Component
public class Mediator implements IMediator {

    private final ICommandHandler<SaveRecordsCommand> SaveRecordsCommandHandler;
    private final ICommandHandler<StopDeadDronesCommand> CheckFlyingDronesCommandHandler;

    public Mediator(ICommandHandler<SaveRecordsCommand> SaveRecordsCommandHandler, ICommandHandler<StopDeadDronesCommand> CheckFlyingDronesCommandHandler) {
        this.SaveRecordsCommandHandler = SaveRecordsCommandHandler;
        this.CheckFlyingDronesCommandHandler = CheckFlyingDronesCommandHandler;
    }

    @Override
    public void send(ICommand command) {
        if (command instanceof SaveRecordsCommand){
            SaveRecordsCommandHandler.handle((SaveRecordsCommand) command);
        } else if (command instanceof StopDeadDronesCommand) {
            CheckFlyingDronesCommandHandler.handle((StopDeadDronesCommand) command);
        } else {
            throw new IllegalArgumentException("Unsupported command");
        }
    }
}
