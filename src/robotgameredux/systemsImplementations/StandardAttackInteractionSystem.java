package robotgameredux.systemsImplementations;

import java.io.Serializable;

import Exceptions.InvalidTargetException;
import robotgameredux.Commands.AtkInteractCommand;
import robotgameredux.actors.Robot;
import robotgameredux.core.GameWorld;
import robotgameredux.core.Vector2;
import robotgameredux.input.RobotStates;
import robotgameredux.systemInterfaces.AttackInteractionSystem;
import robotgameredux.weapons.Weapon;

public class StandardAttackInteractionSystem implements AttackInteractionSystem, Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7545651865171365666L;

	public StandardAttackInteractionSystem(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}

	@Override
	public Boolean execute(AtkInteractCommand command) throws InvalidTargetException {
		Vector2 target = command.getTarget();
		
		switch (command.getState()) {
		case DESTROY_OBSTACLE: 
			if (gameWorld.isObstacle(target) != null && command.getCoords().dst(target) <= 1.5) {
				if(!gameWorld.destroyObstacle(target, command.getStrenght())){
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
		case TAKE_WEAPON:
			if(gameWorld.isStation(target) && command.getCoords().dst(target) <= 1.5) {
				Weapon w = gameWorld.getWeapon();
				if (w != null) {
					command.addWeapon(w);
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
