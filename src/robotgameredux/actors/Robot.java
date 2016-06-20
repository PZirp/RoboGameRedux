package robotgameredux.actors;

import robotgameredux.core.Vector2;
import robotgameredux.graphic.Sprite;
import robotgameredux.graphic.Visual;
import robotgameredux.input.Command;
import robotgameredux.input.Faction;
import robotgameredux.input.RobotStates;
import robotgameredux.systems.MovementSystem;
import robotgameredux.weapons.Bullet;
import robotgameredux.weapons.Weapon;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

import Exceptions.InsufficientEnergyException;
import robotgameredux.core.GameManager;
import robotgameredux.core.GameWorld;

public class Robot extends GameObject  {

	public Robot(Vector2 coords, MovementSystem ms){
		super(coords);
		this.movementSystem = ms;
		this.state = RobotStates.INACTIVE;
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
	
	public int getEnergy() {
		return this.energy;
	}
	
	public void setEnergy(int n) {
		energy = energy - n;
	}
	
	public void addEnergy(int energy) {
		System.out.println("Eccomi qui!!!!!");

		this.energy = this.energy + energy;
	}
	
	

	public void setState(RobotStates state) {
		this.state = state;
		propertyChange.firePropertyChange(this.state.toString(), null, this);
	}
	
	public RobotStates getState() {
		return this.state;
	}
	
	public void setSprite(Sprite sprite) {
		/*fare meglio, non mi piace passare sia la sprite che il riferimento
		*sarebbe forse meglio spostare la sprite nel robot specifico?
		*Non posso semplicemente creare la sprite in questa classe generica perch� non userebbe la sprite giusta
		*Quindi devo crearla nella sottoclasse in base al tipo e poi aggiungerla allo schermo
		*/
		this.sprite = sprite;
		//reference.addRobotToScreen(sprite); //Passa la sprite al controllore che comunica con il GameWorld (JPanel) e la aggiunge sullo schermo
	}
	
	public Sprite getSprite(){
		return sprite;
	}
	
	public int getHealth() {
		return this.health;
	}
	
	//Update health serve per gli oggetti di guarigione
	
	public void updateHealth(int health) {
		int newHealth = this.health + health;
		if (newHealth > 1000) 
			this.health = 100;
		else 
			this.health = newHealth;
	}
	
	public void damage(int damage) {
		int effectiveDamage = damage - this.defense;
		this.health = this.health - effectiveDamage;
		//if energy <25% throw critical status exception
		if (this.health <= 0) {
			//set dead
		}
	}
	
	public Faction getFaction() {
		return this.faction;
	}
	
	public void setFaction(Faction faction) {
		this.faction = faction;
	}
	
	/*public void setTarget(Vector2 target) {
		this.target = target;
	}
	
	public Vector2 getTarget() {
		return this.target;
	}*/

	
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

	private Command currentCommand;
	private MovementSystem movementSystem;
	private Faction faction;
	private RobotStates state;
	private int health;
	private int range = 5;
	private int energy = 100;
	private int defense = 5;
	private int strenght = 10;
	private Sprite sprite;
	private PropertyChangeSupport propertyChange;

}

