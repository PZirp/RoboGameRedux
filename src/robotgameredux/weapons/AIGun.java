package robotgameredux.weapons;

import java.io.Serializable;

public class AIGun implements Weapon, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7239547261958293482L;
	
	public AIGun() {
		bullets = 9999;
		damage = 10;
		shieldPenetration = 5;
		cost = 5;
		type = WeaponType.OFFENSIVE;
	}
	
	public IBullet fire() {
			PistolBullet pro = new PistolBullet(damage, shieldPenetration);
			bullets--;
			return pro;
	}
	
	public Boolean hasBullets() {
		if (bullets > 0) {
			return true;}
		return false;
	}
	
	public int getBulletCount() {
		return bullets;
	}
	
	public void addBullets(int bullets) {
		this.bullets = this.bullets + bullets;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return getClass().getName() + "[Name = " + name + " Bullets = " + bullets + " ShieldPenetration = " + shieldPenetration + "]";
	}
	
	public Boolean isSameWeapon(Weapon other) {
		if (name.equals(other.getName()))
				return true;
		return false;
	}
	
	public boolean equals(Object otherObject) {
		if (otherObject == null) return false;
		if (getClass() != otherObject.getClass()) return false;
		AIGun other = (AIGun) otherObject;
		return bullets == other.bullets && damage == other.damage && shieldPenetration == other.shieldPenetration && name.equals(other.name);
	}
		
	@Override
	public int getCost() {
		return cost;
	}
	
	public WeaponType getType() {
		return type;
	}
	
	private final String name = "AIGun";
	private int bullets;
	private int damage;
	private int shieldPenetration;
	private int cost;
	private WeaponType type;
	
}
