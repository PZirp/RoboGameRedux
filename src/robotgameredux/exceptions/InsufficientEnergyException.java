package robotgameredux.exceptions;

import java.io.IOException;

import robotgameredux.CommandsInterfaces.Command;

public class InsufficientEnergyException extends IOException {

	private Command command;
	private int reqEnergy;

	public InsufficientEnergyException(Command command, int reqEnergy) {
		super("Not enough energy to execute this command");
		this.command = command;
		this.reqEnergy = reqEnergy;
	}

	public Command getCommand() {
		return this.command;
	}

	public int getRequiredEnergy() {
		return this.reqEnergy;
	}

	public int getResidualEnergy() {
		return command.getEnergy();
	}

}
