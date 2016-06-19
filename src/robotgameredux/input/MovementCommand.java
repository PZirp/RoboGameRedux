package robotgameredux.input;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.actors.Robot;
import robotgameredux.core.Vector2;

public class MovementCommand implements Command{

	public MovementCommand(Robot robot, Vector2 destination) {
		this.destination = destination;
		this.robot = robot;
	}
	
	@Override
	public void execute() throws InvalidTargetException, InsufficientEnergyException {
			robot.setCommand(null);
			robot.getMovementSystem().execute(this);
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
	
	public void setEnergy(int n) {
		robot.setEnergy(n);
	}
	
	public int getRange() {
		return robot.getRange();
	}
	
	private Vector2 destination;
	private Robot robot;


}
