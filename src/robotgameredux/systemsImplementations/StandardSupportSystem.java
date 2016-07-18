package robotgameredux.systemsImplementations;

import java.io.Serializable;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.Commands.RobotSupportCommand;
import robotgameredux.CommandsInterfaces.Command;
import robotgameredux.CommandsInterfaces.SupportCommandInterface;
import robotgameredux.TargetInterfaces.TargetInterface;
import robotgameredux.actors.Faction;
import robotgameredux.actors.Robot;
import robotgameredux.core.IActorManager;
import robotgameredux.core.ActorManager;
import robotgameredux.core.Coordinates;
import robotgameredux.input.RobotStates;
import robotgameredux.systemInterfaces.SupportSystem;
import robotgameredux.tools.UsableTool;

public class StandardSupportSystem implements SupportSystem, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 730914482322382664L;

	public StandardSupportSystem(IActorManager rf) {
		this.actorsManager = rf;
	}

	public <T> Boolean execute(SupportCommandInterface<T> command) throws InvalidTargetException, InsufficientEnergyException {
		Coordinates target = command.getTarget();
		UsableTool tool = command.getActiveTool();
//		TargetInterface<?> targeted = null;
		
		if (command.getEnergy() < tool.getCost()) {
			throw new InsufficientEnergyException(command);
		}
		
		TargetInterface<?> targeted = actorsManager.getTarget(target);
		/*if (actorsManager.isRobot(target) == true) {
			targeted = actorsManager.getTarget(target);
		} else {*/
		if (targeted == null || targeted.getFaction() != command.getFaction()) {
			throw new InvalidTargetException(command);
		}
		
		//if (targeted.getFaction() == command.getFaction()) {
			tool.use(targeted);
			command.removeUsedTool(tool);
			command.removeEnergy(tool.getCost());
			return true;
		/*} else {
			throw new InvalidTargetException(command);
		} */
	}

	private IActorManager actorsManager;

	
}
