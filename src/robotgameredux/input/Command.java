package robotgameredux.input;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.actors.Robot;
import robotgameredux.actors.SupportRobot;

public interface Command {

	public void execute() throws InvalidTargetException, InsufficientEnergyException;
	public void setState(RobotStates state);
}
