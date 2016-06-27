package robotgameredux.systemsImplementations;

import java.io.Serializable;

import Exceptions.InvalidTargetException;
import robotgameredux.Commands.SupInteractCommand;
import robotgameredux.actors.Robot;
import robotgameredux.core.GameWorld;
import robotgameredux.core.Vector2;
import robotgameredux.input.RobotStates;
import robotgameredux.systemInterfaces.SupportInteractionSystem;
import robotgameredux.tools.UsableTool;

public class StandardSupportInteractionSystem implements SupportInteractionSystem, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -225292816060795594L;

	public StandardSupportInteractionSystem(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}

	@Override
	public Boolean execute(SupInteractCommand command) throws InvalidTargetException {
		//Robot robot = command.getRobot();
		Vector2 target = command.getTarget();
		
		switch (command.getState()) {
		case PUSH_OBSTACLE: 	
			if (gameWorld.isObstacle(target) != null && command.getCoords().dst(target) <= 1.5) {
				if(!gameWorld.pushObstacle(target, command.getStrenght(), command.getCoords())) {
					throw new InvalidTargetException(command);
				}
				command.removeEnergy(5);
			} else {
				throw new InvalidTargetException(command);
			}
			return true;
		case RECHARGE:
			if(gameWorld.isStation(target) && command.getCoords().dst(target) <= 1.5){
				Integer charge = gameWorld.recharge();
				if (charge != null)
					command.addEnergy(charge);
			} else {
				throw new InvalidTargetException(command);
			}
			return true;
		case TAKE_OBJECT:
			if(gameWorld.isStation(target) && command.getCoords().dst(target) <= 1.5) {
				UsableTool t = gameWorld.getTool();
				if (t != null) {
					command.addTool(t);
					command.removeEnergy(5);
				}
			} else {
				throw new InvalidTargetException(command);
			}
			return true;
		}
			
		return false;
	}
	
	private GameWorld gameWorld;
}
