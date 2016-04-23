package robotgameredux.actors;

import robotgameredux.core.Vector2;

public abstract class GameObject {

	public GameObject() {};

	public Vector2 getCoords() {
		return coords;
	}
	
	public void setCoords(Vector2 newCoords) {
		coords = newCoords;
	}
	

	public void update() {};
	public void render() {};
	private Vector2 coords;	
	
}
