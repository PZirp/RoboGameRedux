package robotgameredux.actors;

import robotgameredux.core.Vector2;
import robotgameredux.graphic.Visual;
import robotgameredux.input.Faction;
import robotgameredux.input.RobotStates;
import robotgameredux.weapons.Projectile;
import robotgameredux.weapons.Weapon;

import java.util.ArrayList;

import robotgameredux.core.GameWorld;

public class Robot extends GameObject {
//Bisogna passare su la sprite appena creata
	public Robot(RobotController reference, Vector2 coords){
		super(coords);
		this.reference = reference;
		sprite = new Visual(this);
		this.reference.addRobotToScreen(sprite); //Passa la sprite al controllore che comunica con il GameWorld (JPanel) e la aggiunge sullo schermo
		this.state = RobotStates.INACTIVE;
	}
	
	public void render() {
		if (this.getCoords() == oldPos) {
			System.out.print("Tile scelta occupata ");
		}		
		else if(energy == 0) {
			System.out.print("Energia insufficiente ");
		}
		System.out.println("Posizione: X = " + this.getCoords().x + " Y = " + this.getCoords().y);
		System.out.println("Salute del robot: " + health);
		sprite.update();
	}
	
	public void update() {
	}
	
	protected void movementComplete(Vector2 oldPos) {
		this.dest = null;
		this.state = RobotStates.INACTIVE;
		this.reference.updateMap(oldPos, this.getCoords());
	}
		
	protected void move() {
		//Oldpos deve diventare variabile interna, non c'è bisogno di tenerla come variabile di istanza (per adesso è di istanza solo per la stampa). Oppure si?
		if (energy > 0) { 
			if(dest.dst(this.getCoords()) < range) {
				oldPos = this.getCoords();
				//Da spostare nel controller?
				this.setCoords(dest);
				//energy--;
				movementComplete(oldPos);
			} else {
				System.out.println("Movimento impossibile, supera il range");
			}
			energy = energy - 10;
			this.setState(RobotStates.INACTIVE);
		} else {
			this.setState(RobotStates.INACTIVE);
		}
	}
	
	
	

	public int getEnergy(){
		return this.energy;
	}
	
	public void setDest(Vector2 dest) {
		this.dest = dest;
	}
	
	public void setState(RobotStates state) {
		this.state = state;
	}
	
	public RobotStates getState() {
		return this.state;
	}
	
	public Visual getSprite(){
		return sprite;
	}
	
	public int getHealth() {
		return this.health;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
	
	public Faction getFaction() {
		return this.faction;
	}
	
	public void setTarget(Vector2 target) {
		this.target = target;
	}
	
	public Vector2 getTarget() {
		return this.target;
	}
	
	private RobotController reference;
	private Faction faction;
	private RobotStates state;
	private int health = 100;
	private int range = 5;
	private int energy = 100;
	private Vector2 oldPos;
	private Vector2 dest;
	private Vector2 target;
	private Visual sprite;
}

