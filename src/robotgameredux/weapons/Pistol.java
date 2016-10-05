package robotgameredux.weapons;

import java.io.Serializable;

import robotgameredux.enums.WeaponType;

public class Pistol implements Weapon, Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7260939238210649882L;

	public Pistol() {
		bullets = 10;
		damage = 10;
		shieldPenetration = 5;
		cost = 5;
		type = WeaponType.OFFENSIVE;
	}
	/*
	 * public int getDamage() { return this.damage; }
	 */

	@Override
	public IBullet fire() {
		PistolBullet pro = new PistolBullet(damage, shieldPenetration);
		bullets--;
		return pro;
	}

	@Override
	public Boolean hasBullets() {
		if (bullets > 0) {
			return true;
		}
		return false;
	}

	@Override
	public int getBulletCount() {
		return bullets;
	}

	@Override
	public void addBullets(int bullets) {
		this.bullets = this.bullets + bullets;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getClass().getName() + "[Name = " + name + " Bullets = " + bullets + " ShieldPenetration = "
				+ shieldPenetration + "]";
	}

	@Override
	public Boolean isSameWeapon(Weapon other) {
		if (name.equals(other.getName()))
			return true;
		return false;
	}

	@Override
	public boolean equals(Object otherObject) {
		if (otherObject == null)
			return false;
		if (getClass() != otherObject.getClass())
			return false;
		Pistol other = (Pistol) otherObject;
		return bullets == other.bullets && damage == other.damage && shieldPenetration == other.shieldPenetration
				&& name.equals(other.name);
	}

	@Override
	public int getCost() {
		return cost;
	}

	@Override
	public WeaponType getType() {
		return type;
	}

	@Override
	public Pistol clone() {
		try {
			Pistol clone = (Pistol) super.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	private final String name = "Pistola";
	private int bullets;
	private int damage;
	private int shieldPenetration;
	private int cost;
	private WeaponType type;

}
