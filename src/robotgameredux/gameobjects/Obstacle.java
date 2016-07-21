package robotgameredux.gameobjects;

import robotgameredux.core.Coordinates;
import robotgameredux.graphic.ObstacleSprite;

public class Obstacle extends GameObject {

	public Obstacle(Coordinates coords) {
		super(coords);
	}

	/**
	 * Ritorna il peso dell'ostacolo da usare quando l'ostacolo deve essere
	 * spostato
	 * 
	 * @return il peso
	 */

	public int getWeight() {
		return this.weight;
	}

	/**
	 * Ritorna la resistanza dell'ostacolo, da usare quando l'ostacolo è
	 * l'obiettivo di un attacco
	 * 
	 * @return il peso
	 */

	public int getResistence() {
		return this.resistence;
	}

	/**
	 * Causa l'update della sprite dell'ostacolo
	 */

	@Override
	public void render() {
		sprite.update();
	}

	/**
	 * Ritorna la sprite associata a questo ostacolo
	 * 
	 * @return la sprite
	 */

	public ObstacleSprite getSprite() {
		return sprite;
	}

	/**
	 * Associa una sprite all'ostacolo
	 * 
	 * @param sprite
	 * 		la sprite da associare
	 */

	public void setSprite(ObstacleSprite sprite) {
		this.sprite = sprite;
	}

	// No setWeight o setResistence perchè sono valori pre-impostati non
	// modificabili

	@Override
	public String toString() {
		return super.toString() + " [Resistence = " + resistence + " Weight = " + weight + "]";
	}

	@Override
	public boolean equals(Object otherObject) {
		if (!super.equals(otherObject))
			return false;
		Obstacle other = (Obstacle) otherObject;
		// Anche qui, aggiungere gli equals la sprite
		return resistence == other.resistence && weight == other.weight;
	}

	@Override
	public Obstacle clone() {
		Obstacle clone = (Obstacle) super.clone();
		clone.resistence = resistence;
		clone.weight = weight;
		clone.sprite = sprite;
		// Ricordati di fare clone per i sistemi
		return clone;
	}

	@Override
	public void update() {
	};

	transient private ObstacleSprite sprite;
	private int resistence = 7;
	private int weight = 5;

}