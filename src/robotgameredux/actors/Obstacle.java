package robotgameredux.actors;

import robotgameredux.core.GameWorld;
import robotgameredux.core.Vector2;

/*
 * Penso che il controllore degli ostacoli e delle stazioni sarà il GameWorld stesso
 */


public class Obstacle extends GameObject{
	
	public Obstacle (GameWorld reference) {
		this.reference = reference;
	}
	
	public int getWeight() {
		return this.weight;
	}
	
	public int getResistence() {
		return this.resistence;
	}
	
	public void render() {
		System.out.println("Posizione dell'ostacolo: " + this.getCoords().toString());
	}
	
	public void update() {
		
		 if (beingPushed) {
			 Vector2 dest;
			 switch(direction) {
			 	case 1: //Spinto verso destra
			 		dest = new Vector2(this.getCoords().x, this.getCoords().y+1);
			 		if (reference.isTileFree(dest)) {
						reference.releaseTile(this.getCoords());
						this.setCoords(dest);
						reference.occupyTile(this.getCoords());
			 		}
			 	case 2: //Spinto verso sinistra
			 		dest = new Vector2(this.getCoords().x, this.getCoords().y-1);
			 		if (reference.isTileFree(dest)) {
						reference.releaseTile(this.getCoords());
						this.setCoords(dest);
						reference.occupyTile(this.getCoords());
			 		}
			 	case 3: //Spinto verso l'alto
			 		dest = new Vector2(this.getCoords().x+1, this.getCoords().y);
			 		if (reference.isTileFree(dest)) {
						reference.releaseTile(this.getCoords());
						this.setCoords(dest);
						reference.occupyTile(this.getCoords());
			 		}
			 	case 4: //Spinto verso il basso
			 		dest = new Vector2(this.getCoords().x-1, this.getCoords().y);
			 		if (reference.isTileFree(dest)) {
						reference.releaseTile(this.getCoords());
						this.setCoords(dest);
						reference.occupyTile(this.getCoords());
			 		}
			 }
			 beingPushed = false;
		 }
		
	}
	
	public void push(int strenght, int direction) {
	
		if (strenght >= weight) {
			this.direction = direction;
			beingPushed = true;
		}
		
	}
	
	// No setWeight o setResistence perchè sono valori pre-impostati non modificabili
	
	private final int resistence = 5;
	private final int weight = 5;
	private Boolean beingPushed = false;
	private int direction;
	private GameWorld reference;
	
}