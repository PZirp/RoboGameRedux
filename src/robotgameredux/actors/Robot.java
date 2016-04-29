package robotgameredux.actors;

import robotgameredux.core.Vector2;
import robotgameredux.graphic.Visual;
import robotgameredux.input.RobotStates;
import robotgameredux.core.GameWorld;

public class Robot extends GameObject {
//Bisogna passare su la sprite appena creata
	public Robot(RobotController reference){
		this.reference = reference;
		sprite = new Visual(this);
		this.reference.addToScreen(sprite); //Passa la sprite al controllore che comunica con il GameWorld (JPanel) e la aggiunge sullo schermo
		this.state = RobotStates.INACTIVE;
		System.out.println("Alla creazione del robot:");
		System.out.println(this.state);
	}
	
	public void render() {
		if (this.getCoords() == oldPos) {
			System.out.print("Tile scelta occupata ");
		}		
		else if(energy == 0) {
			System.out.print("Energia insufficiente ");
		}
		System.out.println("Posizione: X = " + this.getCoords().x + " Y = " + this.getCoords().y);
		sprite.update();
	}
	
	public void update() {
		if (state == RobotStates.MOVING) {
			move(this.dest);
		} else if (state == RobotStates.ATTACKING){
			/*
			 * attack(this.target);
			 * target = null;
			 * state = RobotStates.INACTIVE;
			 */
		}
	}
	
	private void attack(Vector2 target) {
		
	}
	
	private void movementComplete(Vector2 oldPos) {
		this.dest = null;
		this.state = RobotStates.INACTIVE;
		this.reference.updateMap(oldPos, this.getCoords());
	}
		
	public void move(Vector2 dest) {
		//Oldpos deve diventare variabile interna, non c'è bisogno di tenerla come variabile di istanza (per adesso è di istanza solo per la stampa)
		if (energy != 0) { 
			if(dest.dst(this.getCoords()) < range) {
				oldPos = this.getCoords();
				//Da spostare nel controller?
				if(dest.x == this.getCoords().x && dest.y == this.getCoords().y) {
					System.out.println("Sei già sulla tile scelta");
				}
				else {
					this.setCoords(dest);
					energy--;
					movementComplete(oldPos);
				}
			} else {
				System.out.println("Movimento impossibile, supera il range");
			}
			this.setState(RobotStates.INACTIVE);
		}
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
	
	private RobotController reference;
	private RobotStates state;
	private int health = 100;
	private int actionPoints = 2;
	private int range = 5;
	private int strenght = 10;
	private int energy = 100;
	private Vector2 oldPos;
	private Vector2 dest;
	private Vector2 target;
	private Visual sprite;
}

