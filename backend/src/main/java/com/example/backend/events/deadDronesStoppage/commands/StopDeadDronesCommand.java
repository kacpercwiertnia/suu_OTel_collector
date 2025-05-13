package com.example.backend.events.deadDronesStoppage.commands;

import com.example.backend.events.mediator.ICommand;
import com.example.backend.simulatorIntegration.model.DroneFromSimulator;

import java.util.List;

public record StopDeadDronesCommand(List<DroneFromSimulator> drones) implements ICommand { }