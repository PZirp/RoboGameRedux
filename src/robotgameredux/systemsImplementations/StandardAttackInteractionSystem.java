package robotgameredux.systemsImplementations;

import java.io.Serializable;

import robotgameredux.CommandsInterfaces.AtkInteractCommandInterface;
import robotgameredux.core.Coordinates;
import robotgameredux.core.IGameWorld;
import robotgameredux.enums.RobotStates;
import robotgameredux.exceptions.InsufficientEnergyException;
import robotgameredux.exceptions.InvalidTargetException;
import robotgameredux.systemInterfaces.AttackInteractionSystem;
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
	public <T> Boolean execute(AtkInteractCommandInterface<T> command)
			throws InvalidTargetException, InsufficientEnergyException {
		Coordinates target = command.getTarget();

		switch (command.getState()) {
		case DESTROY_OBSTACLE:
			if (command.getEnergy() < interactionCost) {
				throw new InsufficientEnergyException(command, interactionCost);
			}
			// != null
			if (gameWorld.isObstacle(target) == true && command.getCoords().dst(target) <= 1) {
				if (!gameWorld.destroyObstacle(target, command.getStrenght())) {
					throw new InvalidTargetException(command);
				}
				command.removeEnergy(interactionCost);
			} else {
				throw new InvalidTargetException(command);
			}
			return true;
		case RECHARGE:
			if (gameWorld.isStation(target) && command.getCoords().dst(target) <= 1) {
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
				throw new InsufficientEnergyException(command, interactionCost);
			}
			if (gameWorld.isStation(target) && command.getCoords().dst(target) <= 1) {
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

	@Override
	public String toString() {
		return getClass().getName() + "[GameWorld: " + gameWorld.toString() + " InteractionCost: " + interactionCost;
	}

	@Override
	public StandardAttackInteractionSystem clone() {
		try {
			StandardAttackInteractionSystem clone = (StandardAttackInteractionSystem) super.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	@Override
	public boolean equals(Object otherObject) {
		if (otherObject == null)
			return false;
		if (getClass() != otherObject.getClass())
			return false;
		StandardAttackInteractionSystem other = (StandardAttackInteractionSystem) otherObject;
		return interactionCost == other.interactionCost && gameWorld.equals(other.gameWorld);
	}

	private IGameWorld gameWorld;
	private final int interactionCost;

}
