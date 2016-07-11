package Exceptions;

import java.io.IOException;

import robotgameredux.CommandsInterfaces.Command;
import robotgameredux.actors.Robot;

public class InvalidTargetException extends IOException {

	private Command command;
	
	public InvalidTargetException(Command command) {
		super("Invalid target");
		this.command  = command;
	}
	
	public InvalidTargetException(Command command, String msg) {
		super(msg);
		this.command  = command;
	}
	
	public Command getCommand() {
		return this.command;
	}
	
}
