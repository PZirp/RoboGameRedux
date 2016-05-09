package robotgameredux.core;

public class ActionObject {

	public ActionObject(Vector2 origin, int strenght, Vector2 target) {
		this.origin = origin;
		this.strenght = strenght;
		this.target = target;
	}
	
	public Vector2 getOrigin() {
		return origin;
	}
	
	public int getStrenght() {
		return strenght;
	}
	
	public Vector2 getTarget() {
		return target;
	}
	
	public void setTarget(Vector2 target) {
		this.target = target;
	}
	
	private Vector2 origin;
	private int strenght;
	private Vector2 target;
	
}
