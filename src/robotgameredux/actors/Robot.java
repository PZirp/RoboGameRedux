package robotgameredux.actors;

import robotgameredux.core.Vector2;
import robotgameredux.graphic.Sprite;
import robotgameredux.graphic.Visual;
import robotgameredux.input.Faction;
import robotgameredux.input.RobotStates;
import robotgameredux.weapons.Bullet;
import robotgameredux.weapons.Weapon;

import java.util.ArrayList;

import robotgameredux.core.GameWorld;
import robotgameredux.core.MovementSystem;

public class Robot extends GameObject {
//Bisogna passare su la sprite appena creata
	public Robot(GameWorld reference, Vector2 coords, MovementSystem ms){
		super(coords);
		this.movementSystem = ms;
		this.reference = reference;
		//sprite = new Visual(this);
		this.state = RobotStates.INACTIVE;
	}
	
	public void render() {
		/*if (this.getCoords() == oldPos) {
		*	System.out.print("Tile scelta occupata ");
		}*/		
		if(energy == 0) {
			System.out.print("Energia insufficiente ");
		}
		System.out.println("Posizione: X = " + this.getCoords().x + " Y = " + this.getCoords().y);
		System.out.println("Salute del robot: " + health);
		sprite.update();
	}
	
	//abstract public void update();
	
	//Invece di avere due riferimenti allo stesso controllore, semplicemente passare AttackController  a questi metodi qui, che lo
	//prenderanno come RobotController generico nei parametri, senza cast e senza doppio riferimento
	//AbstractRobot non ha bisogno di riferimenti perchè comunque non è mai concretizzato
	
	/*protected void movementComplete(Vector2 oldPos) {
		this.destination = null;
		this.state = RobotStates.INACTIVE;
		reference.releaseTile(oldPos);
		reference.occupyTile(this.getCoords());
	}*/
		
	
	
	protected void move() {
		//Oldpos deve diventare variabile interna, non c'è bisogno di tenerla come variabile di istanza (per adesso è di istanza solo per la stampa). Oppure si?
		/*if (energy > 0) { 
			if(destination.dst(this.getCoords()) < range) {
				Vector2 oldPos = this.getCoords();
				this.setCoords(destination);
				//energy--;
				movementComplete(oldPos);
			} else {
				System.out.println("Movimento impossibile, supera il range");
			}
			//energy = energy - 10;
			//this.setState(RobotStates.INACTIVE);
		} else {
			//this.setState(RobotStates.INACTIVE);
		}*/
		movementSystem.move(this);
	}
	
	public int getEnergy(){
		return this.energy;
	}
	
	public void setDest(Vector2 dest) {
		this.destination = dest;
	}
	
	public void setState(RobotStates state) {
		this.state = state;
	}
	
	public RobotStates getState() {
		return this.state;
	}
	
	public void setSprite(Sprite sprite) {
		/*fare meglio, non mi piace passare sia la sprite che il riferimento
		*sarebbe forse meglio spostare la sprite nel robot specifico?
		*Non posso semplicemente creare la sprite in questa classe generica perchè non userebbe la sprite giusta
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
	
	public void updateHealth(int health) {
		int newHealth = this.health + health;
		if (newHealth > 1000) 
			this.health = 100;
		else 
			this.health = newHealth;
	}
	
	public void damage(int damage) {
		int effectiveDamage = damage - this.defence;
		this.health =- effectiveDamage;
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
	
	public void setTarget(Vector2 target) {
		this.target = target;
	}
	
	public Vector2 getTarget() {
		return this.target;
	}
	
	public GameWorld getReference() {
		return reference;
	}
	
	public int getRange(){
		return range;
	}
	
	public Vector2 getDest(){
		return destination;
	}
	
	private MovementSystem movementSystem;
	private GameWorld reference;
	private Faction faction;
	private RobotStates state;
	private int health = 100;
	private int range = 5;
	private int energy = 100;
	private int defence = 10;
	//private Vector2 oldPos;
	private Vector2 destination;
	private Vector2 target;
	private Sprite sprite;
}

