package robotgameredux.systemsImplementations;

import java.io.Serializable;

import robotgameredux.CommandsInterfaces.InteractCommandInterface;
import robotgameredux.core.Coordinates;
import robotgameredux.core.IGameWorld;
import robotgameredux.enums.RobotStates;
import robotgameredux.exceptions.InsufficientEnergyException;
import robotgameredux.exceptions.InvalidTargetException;
import robotgameredux.systemInterfaces.InteractionSystem;
import robotgameredux.weapons.Weapon;

/**
 * Classe che implementa una modalit� per eseguire un comando di interazione per classi di attacco.
 * Quando un attore di supporto viene creato, � possibile aggiungere questo sistema se si desidera 
 * che il proprio attore abbia la capacit� di interagire con l'ambiente in veste di classe di attacco.
 * Questo sistema permette di interagira con l'ambiente in tre modi:
 * 1) Distruggere un ostacolo, se l'attore ha abbastanza forza ed lo spostamento � consentito
 * 2) Prendere oggetti di tipo Weapon dalla stazione di ricarica
 * 3) Ricaricare l'energia di un attore tramite stazione di ricarica
 * In tutti e tre i casi l'attore deve essere adiacente all'obiettivo. In questa implementazione,
 * ogni azione ha un costo fisso. 
 * 
 * Questa classe implementa l'interfaccia InteractionSystem definendo comportamenti per classi di attacco.
 * 
 * @author Paolo Zirpoli
 */

public class AttackInteractionSystem implements InteractionSystem, Cloneable, Serializable {

	private static final long serialVersionUID = 7545651865171365666L;

	public AttackInteractionSystem(IGameWorld gameWorld) {
		this.gameWorld = gameWorld;
		interactionCost = 5;
	}

	/**
	 * Esegue il comando ricevuto. 
	 * Analizza lo stato ricevuto col comando per decidere quale azione eseguire.
	 * Le azioni possibili sono distruggere l'ostacolo, ricaricare l'attore e prendere un'arma
	 * Se l'attore non ha abbastanza energia per compiere l'azione scelta, lancia InsufficientEnergyException.
	 * Se il target specificato non rientra tra quelli definiti, lancia InvalidTargetException.
	 * Una volta eseguito il comando rimuove una quantit� fissata di energia dall'attore
	 * @param command 
	 * 				Interfaccia che rappresenta il comando di interazione da eseguire 
	 * @throws InsufficientEnergyException
	 * 				Se non c'� energia sufficiente
	 * @throws InvalidTargetException
	 * 				Se il target non � valido
	 */
	
	@Override
	public <T> Boolean execute(InteractCommandInterface<T> command)
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
	public AttackInteractionSystem clone() {
		try {
			AttackInteractionSystem clone = (AttackInteractionSystem) super.clone();
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
		AttackInteractionSystem other = (AttackInteractionSystem) otherObject;
		return interactionCost == other.interactionCost && gameWorld.equals(other.gameWorld);
	}

	private IGameWorld gameWorld;
	private final int interactionCost;

}
