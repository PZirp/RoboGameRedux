package robotgameredux.weapons;

import robotgameredux.actors.Robot;
import robotgameredux.core.Vector2;
import robotgameredux.weapons.Bullet;
public class Weapon {
	
	//Trasformare in una interfaccia?
	
	public Weapon(Robot ownerBot) {
		this.onwerBot = ownerBot;
	}
	
	public int getDamage() {
		return this.damage;
	}
	
	public Bullet fire() {
			Bullet pro = new Bullet(damage);
			bullets--;
			return pro;
	}
	
	public Boolean hasBullets() {
		if (bullets > 0) {
			return true;}
		return false;
	}
	
	private Robot onwerBot;
	private int bullets = 10;
	private int damage = 10;
	
}
