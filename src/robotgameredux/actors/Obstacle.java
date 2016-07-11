package robotgameredux.actors;


import robotgameredux.core.Coordinates;
import robotgameredux.graphic.ObstacleSprite;


public class Obstacle extends GameObject{
	
	public Obstacle (Coordinates coords) {
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
	
	public void update() {};
	
	transient private ObstacleSprite sprite;
	private int resistence = 7;
	private int weight = 5;

}