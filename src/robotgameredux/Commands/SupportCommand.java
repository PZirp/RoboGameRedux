package robotgameredux.Commands;

import java.io.Serializable;

import Exceptions.InvalidTargetException;
import robotgameredux.actors.Faction;
import robotgameredux.actors.Robot;
import robotgameredux.actors.Support;
import robotgameredux.actors.SupportRobot;
import robotgameredux.core.Vector2;
import robotgameredux.input.RobotStates;
import robotgameredux.tools.UsableTool;

public class SupportCommand implements Command, Serializable {

	public SupportCommand(Integer activeObjectIndex, Vector2 target, Support robot) {
		this.activeObjectIndex = activeObjectIndex;
		this.target = target;
		this.robot = robot;
	}
	
	@Override
	public Boolean execute() throws InvalidTargetException{
		robot.setCommand(null);
		return robot.getSupportSystem().execute(this);
		
	}
	
	public Integer getActiveObjectIndex() {
		return activeObjectIndex;
	}

	public UsableTool getActiveTool(int i) {
		return robot.getActiveTool(i);
	}
	
	public Faction getFaction() {
		return robot.getFaction();
	}
	
	public Vector2 getTarget() {
		return target;
	}
	
	public void setState(RobotStates state) {
		robot.setState(state);
	}


	private Integer activeObjectIndex;
	private Vector2 target;
	private Support robot;


	
}