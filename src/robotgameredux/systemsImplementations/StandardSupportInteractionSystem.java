package robotgameredux.systemsImplementations;

import java.io.Serializable;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.Commands.RobotSupportInteractCommand;
import robotgameredux.CommandsInterfaces.SupInteractCommandInterface;
import robotgameredux.actors.Robot;
import robotgameredux.core.GameWorld;
import robotgameredux.core.IGameWorld;
import robotgameredux.core.Coordinates;
import robotgameredux.input.RobotStates;
import robotgameredux.systemInterfaces.SupportInteractionSystem;
import robotgameredux.tools.UsableTool;

public class StandardSupportInteractionSystem implements SupportInteractionSystem, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -225292816060795594L;

	public StandardSupportInteractionSystem(IGameWorld gameWorld) {
		this.gameWorld = gameWorld;
		interactionCost = 5;
	}

	@Override
	public <T> Boolean execute(SupInteractCommandInterface<T> command) throws InvalidTargetException, InsufficientEnergyException {
		//Robot robot = command.getRobot();
		Coordinates target = command.getTarget();
		
		switch (command.getState()) {
		case PUSH_OBSTACLE: 	
			if (command.getEnergy() < interactionCost) {
				throw new InsufficientEnergyException(command);
			}
			if (gameWorld.isObstacle(target) != null && command.getCoords().dst(target) <= 1) {
				if(!gameWorld.pushObstacle(target, command.getStrenght(), command.getCoords())) {
					throw new InvalidTargetException(command);
				}
				command.removeEnergy(interactionCost);
			} else {
				throw new InvalidTargetException(command);
			}
			return true;
		case RECHARGE:
			if(gameWorld.isStation(target) && command.getCoords().dst(target) <= 1){
				Integer charge = gameWorld.recharge();
				if (charge != null)
					command.addEnergy(charge);
			} else {
				throw new InvalidTargetException(command);
			}
			return true;
		case TAKE_OBJECT:
			if (command.getEnergy() < interactionCost) {
				throw new InsufficientEnergyException(command);
			}
			if(gameWorld.isStation(target) && command.getCoords().dst(target) <= 1) {
				UsableTool t = gameWorld.getTool();
				if (t != null) {
					command.addTool(t);
					command.removeEnergy(interactionCost);
				} else {
					command.setState(RobotStates.IDLE);
					return false;
				}
			} else {
				throw new InvalidTargetException(command);
			}
			return true;
		}
			
		return false;
	}
	
	private IGameWorld gameWorld;
	private final int interactionCost;
}
