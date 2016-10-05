package robotgameredux.exceptions;

import robotgameredux.enums.Faction;
import robotgameredux.gameobjects.Actor;

public class CriticalStatusException extends RuntimeException {

	private Actor actor;

	public CriticalStatusException(Actor actor) {
		super("Un robot è in condizioni critiche");
		this.actor = actor;
	}

	public CriticalStatusException(String msg) {
		super(msg);
	}
	
	/**
	 * Ritorna la salute residua dell'attore che ha generato questa eccezione
	 * @return health
	 * 			la salute residua
	 */

	public int getResidualHealth() {
		return actor.getHealth();
	}

	/**
	 * Ritorna la fazione dell'attore che ha generato questa eccezione
	 * @return Faction
	 * 			la fazione dell'attore
	 */
	
	public Faction getFaction() {
		return actor.getFaction();
	}
}
