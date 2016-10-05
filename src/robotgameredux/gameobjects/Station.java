package robotgameredux.gameobjects;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

import robotgameredux.core.Coordinates;
import robotgameredux.graphic.StationSprite;
import robotgameredux.tools.UsableTool;
import robotgameredux.weapons.Weapon;

public class Station extends GameObject {

	public Station(Coordinates coords) {
		super(coords);
		tools = new ArrayList<UsableTool>();
		weapons = new ArrayList<Weapon>();
		energyReserve = 10000;
		this.propertyChange = new PropertyChangeSupport(this);
	}

	/**
	 * Aggiunge l'oggetto specificato tramite parametro alla stazione
	 * 
	 * @param o
	 *            l'oggetto da aggiungere
	 */

	public void addObject(UsableTool o) {
		tools.add(o);
	}

	/**
	 * Aggiunge l'arma specificato tramite parametro alla stazione
	 * 
	 * @param w
	 *            l'arma da aggiungere
	 */

	public void addWeapon(Weapon w) {
		weapons.add(w);
	}

	/**
	 * Ritorna l'oggetto all'indice specificato
	 * 
	 * @param i
	 *            l'indice dell'oggetto
	 * @return l'oggetto scelto se presente nell'array degli oggetti, null
	 *         altrimenti
	 */

	public UsableTool getTool(int i) {
		if (!tools.isEmpty())
			return tools.get(i);
		return null;
	}

	/**
	 * Associa una sprite all'istanza della stazione
	 * 
	 * @param sprite
	 */

	public void setSprite(StationSprite sprite) {
		this.sprite = sprite;
	}

	/**
	 * Causa l'update della sprite associata alla stazione
	 */

	@Override
	public void render() {
		sprite.update();
	}

	/**
	 * Ritorna un riferimento alla sprite associata alla stazione
	 * 
	 * @return la sprite
	 */

	public StationSprite getSprite() {
		return sprite;
	}

	/**
	 * Metodo per ricaricare un attore che far richiesta. In questo caso
	 * ricarica di 100 punti energia per ogni uso. Nel caso non ci sia più
	 * energia rimanente, viene generato un PropertyChangeEvent
	 * 
	 * @return se c'è energia disponbile, ritorna il quantitativo specificato,
	 *         null altrimenti
	 */

	public Integer recharge() {
		if (energyReserve > 0) {
			energyReserve = energyReserve - 100;
			return 100;
		}
		if (energyReserve == 0) {
			propertyChange.firePropertyChange("OUT_OF_ENERGY", this, null);
		}
		return null;
	}

	/**
	 * Ritorna l'array degli oggetti presenti nella stazione
	 * 
	 * @return l'array degli oggetti
	 */

	public ArrayList<UsableTool> getTools() {
		if (!tools.isEmpty())
			return tools;
		return null;
	}

	/**
	 * Ritorna l'arma all'indice specificato
	 * 
	 * @param i
	 *            l'indice dell'arma
	 * @return l'arma scelta se presente nell'array delle armi, null altrimenti
	 */

	public Weapon getWeapon(int i) {
		if (!weapons.isEmpty())
			return weapons.get(i);
		return null;
	}

	/**
	 * Ritorna l'array delle armi presenti nella stazione
	 * 
	 * @return l'array delle armi
	 */

	public ArrayList<Weapon> getWeapons() {
		if (!weapons.isEmpty())
			return weapons;
		return null;
	}

	/*
	 * Non ho fatto in modo che gli oggetti venissero rimossi dall'array quando
	 * viene chiamato getWeapon()/getTool() per evitare side effects Quanto
	 * tools o weapons sono vuoti, viene generato un propertyChangeEvent che fa
	 * in modo che i controllori interattivi non visualizzino più i pulsanti per
	 * prendere oggetti e armi
	 */

	/**
	 * Rimuove l'oggetto specificato dall'indice del parametro. Se a seguito
	 * della rimozione l'array degli oggetti è vuoto, è generato un
	 * PropertyChangeEvent
	 * 
	 * @param i
	 *            l'indice dell'oggetto da rimuovere
	 */

	public void removeTool(int i) {

		if (tools.isEmpty()) {
			propertyChange.firePropertyChange("EMPTY_TOOLS", this, null);
			return;
		}
		tools.remove(i);
	}

	/**
	 * Rimuove l'arma specificata dall'indice del parametro. Se a seguito della
	 * rimozione l'array delle armi è vuoto, è generato un PropertyChangeEvent
	 * 
	 * @param i
	 *            l'indice dell'arma da rimuovere
	 */

	public void removeWeapon(int i) {

		if (weapons.isEmpty()) {
			propertyChange.firePropertyChange("EMPTY_WEAPONS", this, null);
			return;
		}
		weapons.remove(i);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChange.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChange.removePropertyChangeListener(listener);
	}

	@Override
	public String toString() {
		return super.toString() + " [EnergyReserve = " + energyReserve + " Tools = " + tools.toString() + " Weapons = "
				+ weapons.toString() + "]";
	}

	@Override
	public boolean equals(Object otherObject) {
		if (!super.equals(otherObject))
			return false;
		Station other = (Station) otherObject;
		// Anche qui, aggiungere gli equals la sprite
		return energyReserve == other.energyReserve && tools.equals(other.tools) && weapons.equals(other.weapons)
				&& propertyChange.equals(other.propertyChange);
	}

	@Override
	public Station clone() {
		Station clone = (Station) super.clone();
		clone.energyReserve = energyReserve;
		clone.tools = new ArrayList<UsableTool>(tools);
		clone.weapons = new ArrayList<Weapon>(weapons);
		clone.propertyChange = propertyChange;
		// Ricordati di fare clone per i sistemi
		return clone;
	}

	@Override
	public void update() {
	};

	private int energyReserve;
	private ArrayList<UsableTool> tools;
	private ArrayList<Weapon> weapons;
	transient private StationSprite sprite;
	private PropertyChangeSupport propertyChange;

}
