package robotgameredux.actors;

import java.io.Serializable;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.core.Coordinates;

/**
 * Questa classe rappresenta un oggetto di gioco basilare. Ogni oggetto di gioco deve avere una sua posizione.
 * Inoltre, fornire i metodi update() e render(), che devono essere opportunamente implementati per la sottoclasse che 
 * concretizza un GameObject.
 *  
 * @author Paolo Zirpoli
 */

public abstract class GameObject implements Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2357436338766396248L;
	
	public GameObject(Coordinates coords) {
		this.coords = coords;
	}
	
	/**
	 * Ritorna le coordinate correnti del GameObject
	 * @return Le coordinate correnti
	 */
	
	public Coordinates getCoords() {
		return coords;
	}
	
	/**
	 * Setta le coordinate dell'oggetto in base all'argomento
	 * @param le nuove coordinate dell'oggetto
	 */
	
	public void setCoords(Coordinates coords) {
		this.coords = coords;
	}
	
	/**
	 * Ritorna una rappresentazione in forma di stringa dell'oggetto
	 * @return la stringa che rappresenta l'oggetto
	 * @see java.lang.Object#toString()
	 */
	
	
	public String toString() {
		return getClass().getName() + coords.toString();
	}
	
	/**
	 * Effettua un controllo di equivalenza dell'oggetto con il parametro passato
	 * @param l'oggetto da confrontare
	 * @return true se gli oggetti sono uguali, false altrimenti
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	
	public boolean equals(Object otherObject) {
		if (otherObject == null) return false;
		if (getClass() != otherObject.getClass()) return false;
		GameObject other = (GameObject) otherObject;
		return coords.equals(other.coords);
	}
	
	/**
	 * Ritorna un deep-clone di questo oggetto
	 * @return il clone 
	 * @see java.lang.Object#clone()
	 */
	
	public GameObject clone() {
		try {
			GameObject cloned = (GameObject) super.clone();
			cloned.coords = coords.clone();
			return cloned;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
		
	public abstract void update() throws InvalidTargetException, InsufficientEnergyException;
	public abstract void render();
	
	/** Le coordinate dell'oggetto **/	
	private Coordinates coords;	
	
}
