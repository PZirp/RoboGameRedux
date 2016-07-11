package robotgameredux.Commands;

import java.io.Serializable;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.CommandsInterfaces.MovementCommandInterface;
import robotgameredux.actors.Robot;
import robotgameredux.core.Coordinates;
import robotgameredux.input.RobotStates;

public class RobotMovementCommand implements MovementCommandInterface<Robot>, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5621112455719703503L;
	Robot robot;
	Coordinates destination;
		
	public RobotMovementCommand(Robot robot, Coordinates destination) {
		this.robot = robot;
		this.destination = destination;
	}
	
	@Override
	public Boolean execute() throws InvalidTargetException, InsufficientEnergyException {
		return robot.getMovementSystem().execute(this);
	}

	public void resetRobot() {
		robot.setCommand(null);
	}
	
	@Override
	public void setState(RobotStates state) {
		robot.setState(state);
	}

	@Override
	public Coordinates getDestination() {
		return destination;
	}

	@Override
	public Coordinates getCoords() {		
		return robot.getCoords();
	}

	@Override
	public void setCoords(Coordinates coords) {
		robot.setCoords(coords);
	}

	@Override
	public int getEnergy() {
		return robot.getEnergy();
	}

	@Override
	public void removeEnergy(int n) {
		robot.removeEnergy(n);		
	}

	@Override
	public int getRange() {
		return robot.getRange();
	}

}
