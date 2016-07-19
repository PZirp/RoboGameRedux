package robotgameredux.core;

import java.io.Serializable;

import robotgameredux.graphic.Sprite;

public class Tile implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = -427813845990404269L;

	public Tile() {
		this.sprite = null;
		occupied = true;
		this.active = false;
	}

	/**
	 * Imposta la sprite da usare per la tile
	 * 
	 * @param sprite
	 */

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	/**
	 * Ritorna la sprite utilizzata dalla tile
	 * 
	 * @return
	 */

	public Sprite getSprite() {
		return sprite;
	}

	/**
	 * Ritorna lo stato di occupazione della tile.
	 * 
	 * @return ture se occupata, false altrimenti
	 */

	public Boolean isOccupied() {
		return occupied;
	}

	/**
	 * Imposta lo stato della tile in base al parametro indicato
	 * 
	 * @param occupied
	 */

	public void setOccupied(Boolean occupied) {
		this.occupied = occupied;
	}

	/**
	 * Causa l'aggiornamento della sprite della tile
	 */

	public void render() {
		sprite.update();
	}

	/**
	 * Ritorna lo stato di attivazione della tile
	 * 
	 * @return true se attiva, false altrimenti
	 */

	public Boolean isActive() {
		return active;
	}

	/**
	 * Imposta lo stato della tile in base al parametro indicato
	 * 
	 * @param active
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

	private Boolean occupied;
	transient private Sprite sprite;
	private Boolean active;
}
