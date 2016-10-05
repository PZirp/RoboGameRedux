package robotgameredux.weapons;

import robotgameredux.enums.WeaponType;

public interface Weapon {

	// int getDamage();
	IBullet fire();

	Boolean hasBullets();

	int getBulletCount();

	String getName();

	Boolean isSameWeapon(Weapon weapon);

	void addBullets(int bullets);

	int getCost();

	WeaponType getType();

	Weapon clone();
}
