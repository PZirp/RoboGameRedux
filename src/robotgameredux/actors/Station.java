package robotgameredux.actors;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import robotgameredux.core.Coordinates;
import robotgameredux.graphic.StationSprite;
import robotgameredux.tools.EnergyPack;
import robotgameredux.tools.HealthPack;
import robotgameredux.tools.UsableTool;
import robotgameredux.weapons.Pistol;
import robotgameredux.weapons.Weapon;

public class Station extends GameObject{

	public Station(Coordinates coords) {
		super(coords);
		tools = new ArrayList<UsableTool>();
		weapons = new ArrayList<Weapon>();
		energyReserve = 10000;
		this.propertyChange = new PropertyChangeSupport(this);
		weapons.add(new Pistol());
		weapons.add(new Pistol());
		weapons.add(new Pistol());
		tools.add(new HealthPack());
		tools.add(new HealthPack());
		tools.add(new HealthPack());
		tools.add(new EnergyPack());
		tools.add(new EnergyPack());
		tools.add(new EnergyPack());
	}

	public void addObject(UsableTool o) { 
		tools.add(o);
	}

	public void addWeapon(Weapon w) { 
		weapons.add(w);
	}
	
	public UsableTool getTool(int i) {
		if (!tools.isEmpty())
			return tools.get(i);
		return null;
	}
	
	public void setSprite(StationSprite sprite) {
		this.sprite = sprite;
	}	

	public void render() {
		System.out.println("Posizione della stazione: " + this.getCoords().toString());
		sprite.update();
	}
	
	public StationSprite getSprite() {
		return sprite;
	}
	
	public Integer recharge() {
		if (energyReserve > 0) {
			energyReserve = energyReserve - 100;
			return 100;
		}
		if(energyReserve == 0) { 
			propertyChange.firePropertyChange("OUT_OF_ENERGY", this, null);
		}
		return null;
	}
	
	public ArrayList<UsableTool> getTools() {
		if (!tools.isEmpty()) 
			return tools;
		return null;
	}
	
	public Weapon getWeapon(int i) {
		if (!weapons.isEmpty())
			return weapons.get(i);
		return null;
	}
	
	public ArrayList<Weapon> getWeapons() {
		if (!weapons.isEmpty()) 
			return weapons;
		return null;
	}
	
	/*
	 * Non ho fatto in modo che gli oggetti venissero rimossi dall'array quando viene chiamato getWeapon()/getTool() per evitare side effects
	 * Quanto tools o weapons sono vuoti, viene generato un propertyChangeEvent che fa in modo che i controllori interattivi non visualizzino più
	 * i pulsanti per prendere oggetti e armi
	 */
	
	
	public void removeTool(int i) {
		
		if (tools.isEmpty()) {
			propertyChange.firePropertyChange("EMPTY_TOOLS", this, null);
			return;
		}
		tools.remove(i);
	}
	
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

	public String toString() {
		return super.toString() + " [EnergyReserve = " + energyReserve + " Tools = " + tools.toString() + " Weapons = " + weapons.toString() + "]";
	}
    
	
	public boolean equals(Object otherObject) {
		if (!super.equals(otherObject)) return false;
		Station other = (Station) otherObject;
		//Anche qui, aggiungere gli equals la sprite
		return energyReserve == other.energyReserve && tools.equals(other.tools) && weapons.equals(other.weapons) && propertyChange.equals(other.propertyChange);
	}
	
	
	public Station clone() {
		Station clone = (Station) super.clone();
		clone.energyReserve = energyReserve;
		clone.tools = new ArrayList<UsableTool>(tools);
		clone.weapons = new ArrayList<Weapon>(weapons);
		clone.propertyChange = propertyChange;
		//Ricordati di fare clone per i sistemi
		return clone;
	}
	
	public void update() {};
	
	private int energyReserve;
	private ArrayList<UsableTool> tools;
	private ArrayList<Weapon> weapons;
	transient private StationSprite sprite;
	private PropertyChangeSupport propertyChange;

}
 