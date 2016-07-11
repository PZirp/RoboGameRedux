package robotgameredux.systemsImplementations;

import java.io.Serializable;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.Commands.RobotAttackInteractCommand;
import robotgameredux.CommandsInterfaces.AtkInteractCommandInterface;
import robotgameredux.actors.Robot;
import robotgameredux.core.GameWorld;
import robotgameredux.core.IGameWorld;
import robotgameredux.core.Coordinates;
import robotgameredux.input.RobotStates;
import robotgameredux.systemInterfaces.AttackInteractionSystem;
import robotgameredux.weapons.Pistol;
import robotgameredux.weapons.Weapon;

public class StandardAttackInteractionSystem implements AttackInteractionSystem, Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7545651865171365666L;

	public StandardAttackInteractionSystem(IGameWorld gameWorld) {
		this.gameWorld = gameWorld;
		interactionCost = 5;
	}

	@Override
	public <T> Boolean execute(AtkInteractCommandInterface<T> command) throws InvalidTargetException, InsufficientEnergyException {
		Coordinates target = command.getTarget();
		
		switch (command.getState()) {
		case DESTROY_OBSTACLE: 
			if (command.getEnergy() < interactionCost) {
				throw new InsufficientEnergyException(command);
			}
			if (gameWorld.isObstacle(target) != null && command.getCoords().dst(target) <= 1) {
				if(!gameWorld.destroyObstacle(target, command.getStrenght())){
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
				if (charge != null) {
					command.addEnergy(charge);
				}
			} else {
				throw new InvalidTargetException(command);
			}
			return true;
		case TAKE_WEAPON:
			if (command.getEnergy() < interactionCost) {
				throw new InsufficientEnergyException(command);
			}
			if(gameWorld.isStation(target) && command.getCoords().dst(target) <= 1) {
				Weapon w = gameWorld.getWeapon();
				if (w != null) {
					command.addWeapon(w);
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
