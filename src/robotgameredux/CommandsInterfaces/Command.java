package robotgameredux.CommandsInterfaces;

import robotgameredux.enums.RobotStates;
import robotgameredux.exceptions.InsufficientEnergyException;
import robotgameredux.exceptions.InvalidTargetException;

public interface Command {

	public Boolean execute() throws InvalidTargetException, InsufficientEnergyException;

	public void setState(RobotStates state);

	int getEnergy();

	void removeEnergy(int n);

}
