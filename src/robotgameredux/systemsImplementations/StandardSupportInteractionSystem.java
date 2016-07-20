package robotgameredux.systemsImplementations;

import java.io.Serializable;

import robotgameredux.CommandsInterfaces.SupInteractCommandInterface;
import robotgameredux.core.Coordinates;
import robotgameredux.core.IGameWorld;
import robotgameredux.enums.RobotStates;
import robotgameredux.exceptions.InsufficientEnergyException;
import robotgameredux.exceptions.InvalidTargetException;
import robotgameredux.systemInterfaces.SupportInteractionSystem;
import robotgameredux.tools.UsableTool;

/**
 * Classe che implementa una modalità standard per eseguire un comando di interazione per classi di supporto.
 * Quando un attore di supporto viene creato, è possibile aggiungere questo sistema standard se 
 * non si vuole dare un comportamento particolare all'interazione dell'attore.
 * Questo sistema permette di interagira con l'ambiente in tre modi:
 * 1) Spingere un ostacolo, se l'attore ha abbastanza forza ed lo spostamento è consentito
 * 2) Prendere oggetti di tipo UsableTool dalla stazione di ricarica
 * 3) Ricaricare l'energia di un attore tramite stazione di ricarica
 * In tutti e tre i casi l'attore deve essere adiacente all'obiettivo. 
 * 
 * Questa classe implementa l'interfaccia MovementSystem.
 * 
 * @author Paolo Zirpoli
 */


public class StandardSupportInteractionSystem implements SupportInteractionSystem, Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -225292816060795594L;

	public StandardSupportInteractionSystem(IGameWorld gameWorld) {
		this.gameWorld = gameWorld;
		interactionCost = 5;
	}

	@Override
	public <T> Boolean execute(SupInteractCommandInterface<T> command)
			throws InvalidTargetException, InsufficientEnergyException {
		Coordinates target = command.getTarget();

		switch (command.getState()) {
		case PUSH_OBSTACLE:
			if (command.getEnergy() < interactionCost) {
				throw new InsufficientEnergyException(command, interactionCost);
			}
			if (gameWorld.isObstacle(target) == true && command.getCoords().dst(target) <= 1) {
				if (!gameWorld.pushObstacle(target, command.getStrenght(), command.getCoords())) {
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
				if (charge != null)
					command.addEnergy(charge);
			} else {
				throw new InvalidTargetException(command);
			}
			return true;
		case TAKE_OBJECT:
			if (command.getEnergy() < interactionCost) {
				throw new InsufficientEnergyException(command, interactionCost);
			}
			if (gameWorld.isStation(target) && command.getCoords().dst(target) <= 1) {
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

	@Override
	public String toString() {
		return getClass().getName() + "[GameWorld: " + gameWorld.toString() + " InteractionCost: " + interactionCost;
	}

	@Override
	public StandardSupportInteractionSystem clone() {
		try {
			StandardSupportInteractionSystem clone = (StandardSupportInteractionSystem) super.clone();
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
		StandardSupportInteractionSystem other = (StandardSupportInteractionSystem) otherObject;
		return interactionCost == other.interactionCost && gameWorld.equals(other.gameWorld);
	}

	private IGameWorld gameWorld;
	private final int interactionCost;
}
