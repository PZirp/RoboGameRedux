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

	public int getResidualHealth() {
		return actor.getHealth();
	}

	public Faction getFaction() {
		return actor.getFaction();
	}
}
