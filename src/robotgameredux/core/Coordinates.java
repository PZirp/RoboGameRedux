package robotgameredux.core;

import java.io.Serializable;

public final class Coordinates implements Cloneable, Serializable {

	/**
	 * Astrazione che rappresenta una coppia di coordinate su un piano
	 * bidimensionale. <br>
	 * Offre metodi per sottrarre e sommare un altro set di coordinate (utili da
	 * esempio per calcolare la posizione relativa di due oggetti), e un metodo
	 * per calcolarne la distanza. <br>
	 * L'oggetto è immutabile.
	 */
	private static final long serialVersionUID = -6109031109498527081L;

	public final int getX() {
		return x;
	}

	public final int getY() {
		return y;
	}

	/**
	 * Costruisce un oggetto con le coordinate specificate
	 * 
	 * @param x
	 *            La coordinata x
	 * @param y
	 *            La coordinata y
	 */
	public Coordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Substracts the given vector from this vector.
	 * 
	 * @param v
	 *            The vector
	 * @return This vector for chaining
	 */

	public final Coordinates sub(Coordinates v) {
		int x = this.x - v.x;
		int y = this.y - v.y;
		return new Coordinates(x, y);
	}

	/**
	 * Adds the given vector to this vector
	 * 
	 * @param v
	 *            The vector
	 * @return This vector for chaining
	 */
	public final Coordinates add(Coordinates v) {
		int x = this.x + v.x;
		int y = this.y + v.y;
		return new Coordinates(x, y);
	}

	/**
	 * 
	 * @param v
	 *            The other vector
	 * @return the distance between this and the other vector
	 */
	public int dst(Coordinates v) {
		int x_d = v.x - x;
		int y_d = v.y - y;
		return (int) Math.sqrt(x_d * x_d + y_d * y_d);
	}

	@Override
	public String toString() {
		return getClass().getName() + "[X = " + x + " Y = " + y + "]";
	}

	@Override
	public Coordinates clone() {
		try {
			Coordinates clone = (Coordinates) super.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	@Override
	public boolean equals(Object otherObject) {
		if (otherObject == null)
			return false;
		if (getClass() != otherObject.getClass())
			return false;
		Coordinates other = (Coordinates) otherObject;
		return x == other.x && y == other.y;
	}
	

	private final int x;
	private final int y;


}
