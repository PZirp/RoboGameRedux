package robotgameredux.actors;

import robotgameredux.core.GameWorld;
import robotgameredux.core.Vector2;
import robotgameredux.graphic.ObstacleSprite;
import robotgameredux.graphic.Sprite;
import robotgameredux.graphic.Visual;
import robotgameredux.input.RobotStates;

/*
 * Penso che il controllore degli ostacoli e delle stazioni sarà il GameWorld stesso
 */


public class Obstacle extends GameObject{
	
	public Obstacle (Vector2 coords) {
		super(coords);
	}
	
	public int getWeight() {
		return this.weight;
	}
	
	public int getResistence() {
		return this.resistence;
	}
	
	public void render() {
		System.out.println("Posizione dell'ostacolo: " + this.getCoords().toString());
		sprite.update();
	}
	
	public ObstacleSprite getSprite() {
		return sprite;
	}
	
	public void setSprite(ObstacleSprite sprite) {
		this.sprite = sprite;
	}
	
	// No setWeight o setResistence perchè sono valori pre-impostati non modificabili
	
	private ObstacleSprite sprite;
	private final int resistence = 7;
	private final int weight = 5;

}