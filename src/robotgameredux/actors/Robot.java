package robotgameredux.actors;

import robotgameredux.core.Vector2;
import robotgameredux.graphic.Visual;
import robotgameredux.core.GameWorld;

public class Robot extends GameObject {

	public Robot(GameWorld reference){
		this.reference = reference;
		sprite = new Visual(this);
		this.reference.add(sprite);
		this.active = false;
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
		if (state == 1) {
			move(this.dest);
			state = 0;
			active = false;
		}		
	}
		
	public void move(Vector2 dest) {
		
		if (energy != 0) { 
			if(dest.dst(this.getCoords()) < range) {
				oldPos = this.getCoords();
				//System.out.println("Old position: "+ oldPos);
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
	
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	public Boolean getActive() {
		return active;
	}
	
	public void setDest(Vector2 dest) {
		this.dest = dest;
	}
	
	public void setState(int state) {
		this.state = state;
	}
	
	private GameWorld reference;
	private int health = 100;
	private int actionPoints = 2;
	private int range = 5;
	private int strenght = 10;
	private Vector2 oldPos;
	private int energy = 100;
	private Visual sprite;
	private Boolean active;
	private Vector2 dest;
	private int state;
}
