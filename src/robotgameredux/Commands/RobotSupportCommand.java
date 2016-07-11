package robotgameredux.Commands;

import java.io.Serializable;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.CommandsInterfaces.SupportCommandInterface;
import robotgameredux.actors.Faction;
import robotgameredux.actors.Robot;
import robotgameredux.actors.SupportRobot;
import robotgameredux.core.Coordinates;
import robotgameredux.input.RobotStates;
import robotgameredux.tools.UsableTool;

public class RobotSupportCommand implements SupportCommandInterface<SupportRobot>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8562057930386490438L;
	
	public RobotSupportCommand(Integer activeObjectIndex, Coordinates target, SupportRobot robot) {
		this.activeObjectIndex = activeObjectIndex;
		this.target = target;
		this.robot = robot;
	}
	
	@Override
	public Boolean execute() throws InvalidTargetException, InsufficientEnergyException {
		robot.setCommand(null);
		return robot.getSupportSystem().execute(this);
		
	}
	
	public int getEnergy() {
		return robot.getEnergy();
	}
	
	public void removeEnergy(int e) {
		robot.removeEnergy(e);
	}
	
	/*public Integer getActiveObjectIndex() {
		return activeObjectIndex;
	}*/

	public UsableTool getActiveTool() {
		return robot.getActiveTool(activeObjectIndex);
	}
	
	public Faction getFaction() {
		return robot.getFaction();
	}
	
	public Coordinates getTarget() {
		return target;
	}
	
	public void setState(RobotStates state) {
		robot.setState(state);
	}
	
	public void removeUsedTool(UsableTool tool) {
		robot.removeUsedTool(tool);
	}

	private Integer activeObjectIndex;
	private Coordinates target;
	private SupportRobot robot;


	
}