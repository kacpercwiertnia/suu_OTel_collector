package com.example.backend.events.recordRegistration.commands;

import com.example.backend.events.mediator.ICommand;
import com.example.backend.simulatorIntegration.model.DroneFromSimulator;

import java.util.List;

public record SaveRecordsCommand(List<DroneFromSimulator> drones) implements ICommand {}
