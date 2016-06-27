package robotgameredux.actors;

import java.util.ArrayList;

import Exceptions.InvalidTargetException;
import robotgameredux.core.GameWorld;
import robotgameredux.core.Vector2;
import robotgameredux.graphic.ObstacleSprite;
import robotgameredux.graphic.Sprite;
import robotgameredux.graphic.Visual;
import robotgameredux.input.RobotStates;
import robotgameredux.tools.UsableTool;

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
	
	public String toString() {
		return super.toString() + " [Resistence = " + resistence + " Weight = " + weight + "]";
	}
	
	public boolean equals(Object otherObject) {
		if (!super.equals(otherObject)) return false;
		Obstacle other = (Obstacle) otherObject;
		//Anche qui, aggiungere gli equals la sprite
		return resistence == other.resistence && weight == other.weight;
	}
	
	public Obstacle clone() {
		Obstacle clone = (Obstacle) super.clone();
		clone.resistence = resistence;
		clone.weight = weight;
		clone.sprite = sprite;
		//Ricordati di fare clone per i sistemi
		return clone;
	}
	
	
	transient private ObstacleSprite sprite;
	private int resistence = 7;
	private int weight = 5;

}