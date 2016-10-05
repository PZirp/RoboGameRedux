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


	/**
	 * Ritorna il comando che ha generato l'eccezione
	 * @return command
	 * 			il comando
	 */
	
	public Command getCommand() {
		return this.command;
	}


	/**
	 * Ritorna l'energia richiesta dal comando
	 * @return energy
	 * 			l'energia richiesta
	 */
	
	public int getRequiredEnergy() {
		return this.reqEnergy;
	}
	

	/**
	 * Ritorna l'energia residua dell'attore che ha generato questa eccezione
	 * @return energy
	 * 			la l'energia residua
	 */

	public int getResidualEnergy() {
		return command.getEnergy();
	}

}
