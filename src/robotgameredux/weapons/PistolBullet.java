package robotgameredux.weapons;

import java.io.Serializable;

import robotgameredux.TargetInterfaces.TargetInterface;
import robotgameredux.actors.Robot;

public class PistolBullet implements Serializable, IBullet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8084666627917196935L;
	public PistolBullet(int damage, int shieldPenetration) {
		this.dmg = damage;
		this.shieldPenetration = shieldPenetration;
	}
	
	public Integer getDamage() {
		return this.dmg;
	}
	
	public Integer getShieldPenetration() {
		return this.shieldPenetration;
	}
	
	public <T> void hit(TargetInterface<T> target) {
		int actualDamage = dmg - (target.getDefense() - shieldPenetration);
		if (actualDamage < 0) {
			actualDamage = 0;
		}
		target.applyDamage(actualDamage);
	}
	
	private int dmg;
	private int shieldPenetration;
}
