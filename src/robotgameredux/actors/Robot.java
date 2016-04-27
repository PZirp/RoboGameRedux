package robotgameredux.actors;

import robotgameredux.core.Vector2;
import robotgameredux.graphic.Visual;
import robotgameredux.input.RobotStates;
import robotgameredux.core.GameWorld;

public class Robot extends GameObject {

	public Robot(GameWorld reference){
		this.reference = reference;
		sprite = new Visual(this);
		this.reference.add(sprite);
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
		sprite.update();
	}
	
	public void update() {
		if (state == RobotStates.MOVING) {
			move(this.dest);
			dest = null;
			state = RobotStates.INACTIVE;
		}		
	}
		
	public void move(Vector2 dest) {
		//Oldpos deve diventare variabile interna, non c'è bisogno di tenerla come variabile di istanza (per adesso è di istanza solo per la stampa)
		if (energy != 0) { 
			if(dest.dst(this.getCoords()) < range) {
				oldPos = this.getCoords();
				if(dest.x == this.getCoords().x && dest.y == this.getCoords().y) {
					System.out.println("Sei già sulla tile scelta");
				}
				else {
					if(reference.isTileFree(dest)) {
						reference.releaseTile(oldPos);
						this.setCoords(dest);
						energy--;
						reference.occupyTile(this.getCoords());
					}
				}
			} else {
				System.out.println("Movimento impossibile, supera il range");
			}
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
	
	private GameWorld reference;
	private int health = 100;
	private int actionPoints = 2;
	private int range = 5;
	private int strenght = 10;
	private Vector2 oldPos;
	private int energy = 100;
	private Visual sprite;
	private Vector2 dest;
	private RobotStates state;
}
