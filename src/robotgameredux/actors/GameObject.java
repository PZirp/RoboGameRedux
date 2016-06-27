package robotgameredux.actors;

import java.io.Serializable;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.core.Vector2;

public abstract class GameObject implements Cloneable, Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 2357436338766396248L;
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
	
	public boolean equals(Object otherObject) {
		if (otherObject == null) return false;
		if (getClass() != otherObject.getClass()) return false;
		GameObject other = (GameObject) otherObject;
		return coords.equals(other.coords);
	}
	
	public GameObject clone() {
		try {
			GameObject cloned = (GameObject) super.clone();
			cloned.coords = coords.clone();
			return cloned;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
		
	public void update() throws InvalidTargetException, InsufficientEnergyException  {};
	public void render() {};
	private Vector2 coords;	
	
}
