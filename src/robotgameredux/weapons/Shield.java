package robotgameredux.weapons;

import java.io.Serializable;

import robotgameredux.enums.WeaponType;

public class Shield implements Weapon, Serializable, Cloneable {

	public Shield() {
		bullets = 1;
		defense = 10;
		cost = 10;
		type = WeaponType.DEFENSIVE;
	}

	public int getDefense() {
		return defense;
	}

	@Override
	public IBullet fire() {
		bullets--;
		return new ShieldBullet(defense);
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
	public String getName() {
		return name;
	}

	@Override
	public Boolean isSameWeapon(Weapon other) {
		if (name.equals(other.getName()))
			return true;
		return false;
	}

	@Override
	public void addBullets(int bullets) {
		this.bullets = this.bullets + bullets;
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
	public Shield clone() {
		try {
			Shield clone = (Shield) super.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	private final String name = "Scudo";
	private int bullets;
	private int defense;
	private int cost;
	private WeaponType type;

}
