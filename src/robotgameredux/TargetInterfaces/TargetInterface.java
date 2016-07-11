package robotgameredux.TargetInterfaces;

import robotgameredux.actors.Faction;

public interface TargetInterface<T> {

	int getHealth();
	int getDefense();
	void applyDamage(int damage);	
	Faction getFaction();
	void setDefense(int defense);
	int getEnergy();
	void addEnergy(int energy);
	void heal(int health);
	
}
