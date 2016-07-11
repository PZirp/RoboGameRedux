package robotgameredux.actors;

import robotgameredux.core.Coordinates;
import robotgameredux.graphic.Sprite;
import robotgameredux.graphic.Visual;
import robotgameredux.input.RobotStates;
import robotgameredux.systemInterfaces.MovementSystem;
import robotgameredux.weapons.PistolBullet;
import robotgameredux.weapons.Pistol;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import Exceptions.CriticalStatusException;
import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.CommandsInterfaces.Command;
import robotgameredux.core.GameManager;
import robotgameredux.core.GameWorld;

public class Robot extends GameObject  {

	public Robot(Coordinates coords, MovementSystem ms){
		super(coords);
		this.movementSystem = ms;
		this.state = RobotStates.IDLE;
		health = 100;
		this.propertyChange = new PropertyChangeSupport(this);
	}
	
	public void render() {
		
		if(energy == 0) {
			System.out.print("Energia insufficiente ");
		}
		System.out.println("Posizione: X = " + this.getCoords().getX() + " Y = " + this.getCoords().getY());
		System.out.println("Salute del robot: " + health + getClass().getName());
		System.out.println("Energia del robot: " + this.energy);

		sprite.update();
	}
	
	public void update()throws InvalidTargetException, InsufficientEnergyException {
		Boolean res = false;
		if (currentCommand != null) {
			res = currentCommand.execute();
			//JOptionPane.showMessageDialog(null, "AggiornO");
		}		
		if (res == true) {
			setState(RobotStates.TURN_OVER);
		}
		if (energy <= 0 && state != RobotStates.INACTIVE) {
			setState(RobotStates.INACTIVE);
			getPropertyChange().firePropertyChange("DEACTIVATED", this, null);
			return;
		}
	}
	
	/*
	 * Metodi relativi all'energia
	 */
	
	public int getEnergy() {
		return this.energy;
	}
	
	public void removeEnergy(int energy) {
		this.energy = this.energy - energy;
		if (energy <= 0) {
			//this.state = RobotStates.INACTIVE;
			//propertyChange.firePropertyChange("DEACTIVATED", this, null);
		}
	}	
	
	public void addEnergy(int energy) {
		if (this.energy == 0) {
			this.setState(RobotStates.TURN_OVER);
			propertyChange.firePropertyChange("REACTIVATED", this, null);
		}
		this.energy = this.energy + energy;
		if (this.energy > 100) { 
			this.energy = 100;
		}
	}
	
	public void setState(RobotStates state) {
		this.state = state;
		propertyChange.firePropertyChange(this.state.toString(), this, null);
	}
	
	public RobotStates getState() {
		return this.state;
	}
	
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
	
	public Sprite getSprite(){
		return sprite;
	}
	
	/*
	 * Metodi relativi alla salute
	 */
	
	public int getHealth() {
		return this.health;
	}
	
	//heal serve per gli oggetti di guarigione
	
	public void heal(int health) {
		int newHealth = this.health + health;
		if (newHealth > 100) 
			this.health = 100;
		else 
			this.health = newHealth;
	}
		
	public void damage(int damage) throws CriticalStatusException {
		
		this.health = health - damage;
		
		if (health <= 25 && health > 0) {
			throw new CriticalStatusException();
		}
		
		if (this.health <= 0) {
			//Mandare un propertychangedevent che dice che è morto per farlo rimuovere
			propertyChange.firePropertyChange("DESTROYED", this, null);
		}
	}
	
	public int getDefense() {
		return this.defense;
	}
	
	public void setDefense(int defense) {
		int newDefense = this.defense + defense;
		if(newDefense > 20) {
			this.defense = 20;
		} else {
			this.defense = newDefense;
		}		
	}
	
	public Faction getFaction() {
		return this.faction;
	}
	
	public void setFaction(Faction faction) {
		this.faction = faction;
	}
	
	public int getRange(){
		return range;
	}
	
	public int getStrenght() {
		return strenght;
	}
		
	public void addSprite(Sprite sprite) {
		this.sprite = sprite;
	}
	
	public void setCommand(Command command) {
		this.currentCommand = command;
	}
	
	public Command getCurrentCommand() {
		return currentCommand;
	}
	
	public MovementSystem getMovementSystem() {
		return movementSystem;
	}
	
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChange.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChange.removePropertyChangeListener(listener);
    }
    
    public String toString() {
    	return super.toString() + " [Health = " + health + " Range = " + range + " Energy = " + energy + " Defense = " + defense + " Strenght = " + strenght + " Faction = " + faction.toString() + " State = " + state.toString();
    }

    public boolean equals(Object otherObject) {
    	if (!super.equals(otherObject)) return false;
    	Robot other = (Robot) otherObject;
    	//Aggiungere gli equals dei command e dei sistemi
    	return faction.equals(other.faction) && state.equals(other.state) && health == other.health && range == other.range && energy == other.energy && defense == other.defense && strenght == other.strenght && propertyChange.equals(other.propertyChange) && movementSystem == other.movementSystem;
    }
    
    public Robot clone() {
			Robot cloned = (Robot) super.clone();
			cloned.health = health;
			cloned.range = range;
			cloned.energy = energy;
			cloned.defense = defense;
			cloned.strenght = strenght;
			cloned.faction = faction;
			cloned.state = state;
			cloned.propertyChange = propertyChange;
			cloned.movementSystem = movementSystem;
			return cloned;
	}
    
    public PropertyChangeSupport getPropertyChange() {
    	return propertyChange;
    }
    
	private Command currentCommand;
	private MovementSystem movementSystem;
	private Faction faction;
	private RobotStates state;
	private int health;
	private int range = 5;
	private int energy = 100;
	private int defense = 5;
	private int strenght = 10;
	transient private Sprite sprite;
	private PropertyChangeSupport propertyChange;

}

