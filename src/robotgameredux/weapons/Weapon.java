package robotgameredux.weapons;

import java.io.Serializable;

import robotgameredux.actors.AttackRobot;
import robotgameredux.actors.GameObject;
import robotgameredux.actors.Robot;
import robotgameredux.core.Vector2;
import robotgameredux.tools.UsableTool;
import robotgameredux.weapons.Bullet;
public class Weapon implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7260939238210649882L;
	//Trasformare in una interfaccia?
	
	public Weapon() {
		//this.onwerBot = ownerBot;
	}
	
	public int getDamage() {
		return this.damage;
	}
	
	public Bullet fire() {
			Bullet pro = new Bullet(damage, shieldPenetration);
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

	
	public void addTo(AttackRobot robot) {
		robot.addWeapon(this);		
	}
	
	
	public String toString() {
		return getClass().getName() + "[Name = " + name + " Bullets = " + bullets + " ShieldPenetration = " + shieldPenetration + "]";
	}
	
	public boolean isSameWeapon(Weapon other) {
		if (name.equals(other.name))
				return true;
		return false;
	}
	
	public boolean equals(Object otherObject) {
		if (otherObject == null) return false;
		if (getClass() != otherObject.getClass()) return false;
		Weapon other = (Weapon) otherObject;
		return bullets == other.bullets && damage == other.damage && shieldPenetration == other.shieldPenetration && name.equals(other.name);
	}
	
	private String name = "Default";
	//private Robot onwerBot;
	private int bullets = 3;
	private int damage = 50;
	private int shieldPenetration = 0;
	
}
