package robotgameredux.input;

import robotgameredux.actors.Robot;
import robotgameredux.core.Vector2;

public class InteractCommand implements Command {

	public InteractCommand(Robot robot, Vector2 target) {
		this.robot = robot;
		this.target = target;
	}

	@Override
	public void execute() {
		System.out.println("All'interno del comando 0000000000000000000000000000");

		robot.setCommand(null);
		robot.getInteractSystem().execute(this);
	}
	
	public Vector2 getTarget() {
		return target;
	}
	
	public Robot getRobot() {
		return robot;
	}
	
	private Robot robot;
	private Vector2 target;
	
}
