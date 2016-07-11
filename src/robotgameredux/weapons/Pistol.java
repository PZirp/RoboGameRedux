package robotgameredux.weapons;

import java.io.Serializable;

import robotgameredux.actors.AttackRobot;
import robotgameredux.actors.GameObject;
import robotgameredux.actors.Robot;
import robotgameredux.core.Coordinates;
import robotgameredux.tools.UsableTool;
import robotgameredux.weapons.PistolBullet;
public class Pistol implements Weapon, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7260939238210649882L;
	//Trasformare in una interfaccia?
	
	public Pistol() {
		//this.onwerBot = ownerBot;
		bullets = 555;
		damage = 10;
		shieldPenetration = 0;
		cost = 5;
		type = WeaponType.OFFENSIVE;
	}
	/*
	public int getDamage() {
		return this.damage;
	}*/
	
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
		Pistol other = (Pistol) otherObject;
		return bullets == other.bullets && damage == other.damage && shieldPenetration == other.shieldPenetration && name.equals(other.name);
	}
		
	@Override
	public int getCost() {
		return cost;
	}
	
	public WeaponType getType() {
		return type;
	}
	
	private final String name = "Pistol";
	private int bullets;
	private int damage;
	private int shieldPenetration;
	private int cost;
	private WeaponType type;

	
}
