package robotgameredux.actors;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.core.Vector2;

public abstract class GameObject {


	public GameObject(Vector2 coords) {
		this.coords = coords;
	}

	public Vector2 getCoords() {
		return coords;
	}
	
	public void setCoords(Vector2 coords) {
		this.coords = coords;
	}
	
	public String toString() {
		return getClass().getName() + coords.toString();
	}
	

	public void update() throws InvalidTargetException, InsufficientEnergyException  {};
	public void render() {};
	private Vector2 coords;	
	
}
