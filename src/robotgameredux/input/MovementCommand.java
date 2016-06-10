package robotgameredux.input;

import robotgameredux.actors.Robot;
import robotgameredux.core.Vector2;

public class MovementCommand implements Command{

	public MovementCommand(Robot robot, Vector2 destination) {
		this.destination = destination;
		this.robot = robot;
	}
	
	@Override
	public void execute() {
			robot.setCommand(null);
			robot.getMovementSystem().execute(this);
	}		
	

	public Vector2 getDestination() {
		return destination;
	}
	public Robot getRobot() {
		return robot;
	}


	private Vector2 destination;
	private Robot robot;

}
