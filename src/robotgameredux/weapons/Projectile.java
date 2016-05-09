package robotgameredux.weapons;

import robotgameredux.actors.GameObject;
import robotgameredux.core.Vector2;
import robotgameredux.graphic.ProjectileSprite;


public class Projectile extends GameObject{
	
	private Vector2 target;
	private Vector2 origin;
	private Vector2 current;
	private int dmg;
	//private ProjectileSprite sprite;
	
	public Projectile(Vector2 target, Vector2 origin, int dmg) {
		super(origin);
		this.target = target;
		//this.origin = origin;
		this.dmg = dmg;
		this.current = origin;
		//this.sprite = new ProjectileSprite(this);
	}
	/*public void render() {
		sprite.update();
	}
	public ProjectileSprite getSprite() {
		return this.sprite;
	}*/
	
	public Vector2 getTarget() {
		return this.target;
	}
	
	public Vector2 getOrigin() {
		return this.origin;
	}

	public Vector2 getCurrent() {
		return this.current;
	}
	
	public int getDamage() {
		return this.dmg;
	}
}
