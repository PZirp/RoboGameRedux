package robotgameredux.input;

import robotgameredux.actors.Robot;
import robotgameredux.actors.Support;
import robotgameredux.actors.SupportRobot;
import robotgameredux.core.Vector2;
import robotgameredux.systems.GameSystem;

public class SupportCommand implements Command {

	public SupportCommand(Integer activeObjectIndex, Vector2 target, Support robot) {
		this.activeObjectIndex = activeObjectIndex;
		this.target = target;
		this.robot = robot;
	}
	
	@Override
	public void execute() {
		robot.setCommand(null);
		robot.getSupportSystem().execute(this);
	}
	
	public Integer getActiveObjectIndex() {
		return activeObjectIndex;
	}

	public Vector2 getTarget() {
		return target;
	}

	public Support getRobot() {
		return robot;
	}



	private Integer activeObjectIndex;
	private Vector2 target;
	private Support robot;
	@Override
	public void setState(RobotStates state) {
		// TODO Auto-generated method stub
		
	}
	
}