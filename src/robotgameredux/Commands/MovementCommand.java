package robotgameredux.Commands;

import java.io.Serializable;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.actors.Robot;
import robotgameredux.core.Vector2;
import robotgameredux.input.RobotStates;

public class MovementCommand implements Command, Serializable{

	public MovementCommand(Robot robot, Vector2 destination) {
		this.destination = destination;
		this.robot = robot;
	}
	
	@Override
	public Boolean execute() throws InvalidTargetException, InsufficientEnergyException {
			return robot.getMovementSystem().execute(this);
	}		
	
	public void resetRobot() {
		robot.setCommand(null);
	}
	

	public Vector2 getDestination() {
		return destination;
	}
	public Robot getRobot() {
		return robot;
	}
	
	@Override
	public void setState(RobotStates state) {
		robot.setState(state);
	}

	public Vector2 getCoords() {
		return robot.getCoords();
	}
	
	public void setCoords(Vector2 coords) {
		robot.setCoords(coords);
	}
	
	public int getEnergy() {
		return robot.getEnergy();
	}
	
	public void removeEnergy(int n) {
		robot.removeEnergy(n);
	}
	
	public int getRange() {
		return robot.getRange();
	}
	
	private Vector2 destination;
	private Robot robot;


}
