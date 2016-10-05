package robotgameredux.exceptions;

import java.io.IOException;

import robotgameredux.CommandsInterfaces.Command;
import robotgameredux.enums.Faction;

public class InvalidTargetException extends IOException {

	private Command command;

	public InvalidTargetException(Command command) {
		super("Invalid target");
		this.command = command;
	}

	public InvalidTargetException(Command command, String msg) {
		super(msg);
		this.command = command;
	}

	public Command getCommand() {
		return this.command;
	}

	public Faction getFaction() {
		return null;
	}

}
