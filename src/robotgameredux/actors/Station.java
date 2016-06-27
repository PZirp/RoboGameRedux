package robotgameredux.actors;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.TooManyListenersException;

import robotgameredux.core.Vector2;
import robotgameredux.graphic.StationSprite;
import robotgameredux.tools.HealthPack;
import robotgameredux.tools.UsableTool;
import robotgameredux.weapons.Weapon;

public class Station extends GameObject{

	public Station(Vector2 coords) {
		super(coords);
		tools = new ArrayList<UsableTool>();
		weapons = new ArrayList<Weapon>();
		energyReserve = 10000;
		weapons.add(new Weapon());
		weapons.add(new Weapon());
		weapons.add(new Weapon());
		this.propertyChange = new PropertyChangeSupport(this);
		/*tools.add(new HealthPack());
		tools.add(new HealthPack());
		tools.add(new HealthPack());
		tools.add(new HealthPack());
		tools.add(new HealthPack());*/
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
	 */
	
	public void removeTool(int i) {
		tools.remove(i);
		if (tools.isEmpty()) {
			System.out.println("OOOOOOOOOOOOOH SONO QUIIIIIIIIIIIIIII");
			propertyChange.firePropertyChange("EMPTY_TOOLS", this, null);
		}
	}
	
	public void removeWeapon(int i) {
		weapons.remove(i);
		if (weapons.isEmpty()) {
			System.out.println("OOOOOOOOOOOOOH SONO QUIIIIIIIIIIIIIII");
			propertyChange.firePropertyChange("EMPTY_WEAPONS", this, null);
		}
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
	
	
	private int energyReserve;
	private ArrayList<UsableTool> tools;
	private ArrayList<Weapon> weapons;
	transient private StationSprite sprite;
	private PropertyChangeSupport propertyChange;

}
 