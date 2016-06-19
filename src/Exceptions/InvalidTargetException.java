package Exceptions;

import java.io.IOException;

import robotgameredux.actors.Robot;
import robotgameredux.input.Command;

public class InvalidTargetException extends IOException {

	private Command command;
	
	public InvalidTargetException(Command command) {
		super("Invalid target");
		this.command  = command;
	}
	
	public Command getCommand() {
		return this.command;
	}
	
}
