package robotgameredux.weapons;

import java.io.Serializable;

import robotgameredux.actors.Robot;

public class Bullet implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8084666627917196935L;
	public Bullet(int damage, int shieldPenetration) {
		this.dmg = damage;
		this.shieldPenetration = shieldPenetration;
	}
	
	public Integer getDamage() {
		return this.dmg;
	}
	
	public Integer getShieldPenetration() {
		return this.shieldPenetration;
	}
		
	public void hit(Robot robot) {
		//robot.damage(dmg);
	}
	
	private int dmg;
	private int shieldPenetration;
}
