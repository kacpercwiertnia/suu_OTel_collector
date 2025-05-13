package com.example.backend.events.mediator;

public interface ICommandHandler <T extends ICommand>{
    public void handle(T command);
}
