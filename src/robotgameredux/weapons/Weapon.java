package robotgameredux.weapons;

public interface Weapon {
	
	//int getDamage();
	IBullet fire();
	Boolean hasBullets();
	int getBulletCount();
	String getName();
	Boolean isSameWeapon(Weapon weapon);
	void addBullets(int bullets);
	int getCost();
	WeaponType getType();
}
