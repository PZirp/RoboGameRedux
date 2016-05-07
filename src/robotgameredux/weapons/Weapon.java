package robotgameredux.weapons;

import robotgameredux.actors.Robot;
import robotgameredux.core.Vector2;
import robotgameredux.weapons.Projectile;
public class Weapon {
	
	public Weapon(Robot ownerBot) {
		this.onwerBot = ownerBot;
	}
	
	public int getDamage() {
		return this.damage;
	}
	
	
	public Projectile fire(Vector2 target) {
			Projectile pro = new Projectile(target, onwerBot.getCoords(), this.damage);
			bullets--;
			return pro;
	}
	
	public Boolean hasBullets() {
		System.out.println("AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
		if (bullets > 0) {
			System.out.println("HO DEI PROIETTILI USALI");
			return true;}
		return false;
	}
	
	private Robot onwerBot;
	private int bullets = 10;
	private int damage = 10;
	
}
