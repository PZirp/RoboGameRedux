package Exceptions;

import java.io.IOException;

import robotgameredux.Commands.Command;

public class InsufficientEnergyException extends IOException {

private Command command;
	
	public InsufficientEnergyException(Command command) {
		super("Not enough energy to execute this command");
		this.command  = command;
	}
	
	public Command getCommand() {
		return this.command;
	}
	
	
}
