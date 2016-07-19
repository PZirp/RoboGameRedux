package robotgameredux.Commands;

import java.io.Serializable;

import robotgameredux.CommandsInterfaces.MovementCommandInterface;
import robotgameredux.core.Coordinates;
import robotgameredux.enums.RobotStates;
import robotgameredux.exceptions.InsufficientEnergyException;
import robotgameredux.exceptions.InvalidTargetException;
import robotgameredux.gameobjects.Actor;

public class ActorMovementCommand implements MovementCommandInterface<Actor>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5621112455719703503L;
	Actor actor;
	Coordinates destination;

	/**
	 * Esegue questo comando. Tramite il riferimento all'attore che ha ricevuto
	 * il comando, chiama il metodo execute(Command) del sistema a cui si
	 * riferisce questo comando.
	 */

	public ActorMovementCommand(Actor actor, Coordinates destination) {
		this.actor = actor;
		this.destination = destination;
	}

	@Override
	public Boolean execute() throws InvalidTargetException, InsufficientEnergyException {
		return actor.getMovementSystem().execute(this);
	}

	/**
	 * Resetta il comando corrente dell'attore
	 */

	@Override
	public void resetActor() {
		actor.setCommand(null);
	}

	/**
	 * Aggiorna lo stato dall'attore che ha ricevuto il comando
	 * 
	 * @param il
	 *            nuovo stato dell'attore (enum RobotStates)
	 */

	@Override
	public void setState(RobotStates state) {
		actor.setState(state);
	}

	/**
	 * Ritorna le coordinate della destinazione dell'attore
	 * 
	 * @return target - la destinazione dell'attore
	 */

	@Override
	public Coordinates getDestination() {
		return destination;
	}

	/**
	 * Ritorna le coordinate dell'attore che ha ricevuto il comando
	 * 
	 * @param le
	 *            coordinate dell'attore
	 */

	@Override
	public Coordinates getCoords() {
		return actor.getCoords();
	}

	/**
	 * Aggiorna la posizione corrente dell'attore con quella indicata
	 * 
	 * @param la
	 *            nuova poszione dell'attore
	 */

	@Override
	public void setCoords(Coordinates coords) {
		actor.setCoords(coords);
	}

	/**
	 * Ritorna l'energia dall'attore che ha ricevuto il comando
	 * 
	 * @return l'energia dell'attore
	 */

	@Override
	public int getEnergy() {
		return actor.getEnergy();
	}

	/**
	 * Rimuove l'energia (costo dell'azione) dall'attore che ha ricevuto il
	 * comando
	 * 
	 * @param l'energia
	 *            da rimuovere
	 */

	@Override
	public void removeEnergy(int n) {
		actor.removeEnergy(n);
	}

	/**
	 * Ritorna il range di movimento dell'attore che ha ricevuto il comando
	 * 
	 * @return l'energia dell'attore
	 */

	@Override
	public int getRange() {
		return actor.getRange();
	}

}
