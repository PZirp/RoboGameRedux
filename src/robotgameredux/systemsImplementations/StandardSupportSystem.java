package robotgameredux.systemsImplementations;

import java.io.Serializable;

import Exceptions.InvalidTargetException;
import robotgameredux.Commands.Command;
import robotgameredux.Commands.SupportCommand;
import robotgameredux.actors.Faction;
import robotgameredux.actors.Robot;
import robotgameredux.actors.Support;
import robotgameredux.core.RobotFactory;
import robotgameredux.core.Vector2;
import robotgameredux.input.RobotStates;
import robotgameredux.systemInterfaces.SupportSystem;
import robotgameredux.tools.UsableTool;

public class StandardSupportSystem implements SupportSystem, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 730914482322382664L;

	public StandardSupportSystem(RobotFactory rf) {
		this.actorsManager = rf;
	}

	public Boolean execute(SupportCommand command) throws InvalidTargetException {
		
		Integer index = command.getActiveObjectIndex();
		Vector2 target = command.getTarget();
		Robot targeted = actorsManager.isRobot(target);
		UsableTool tool = command.getActiveTool(index);
		
		if (targeted == null || command.getEnergy() < tool.getCost()) {
			throw new InvalidTargetException(command);
		}	
		
		if (targeted.getFaction() == command.getFaction()) {
			tool.use(targeted);
			command.removeUsedTool(tool);
			command.removeEnergy(tool.getCost());
			return true;
		} else {
			throw new InvalidTargetException(command);
		} 
	}

	private RobotFactory actorsManager; 
	
}
