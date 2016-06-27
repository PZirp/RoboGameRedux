package robotgameredux.Commands;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.actors.Robot;
import robotgameredux.actors.SupportRobot;
import robotgameredux.input.RobotStates;

public interface Command {

	public Boolean execute() throws InvalidTargetException, InsufficientEnergyException;
	public void setState(RobotStates state);
}
