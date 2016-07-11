package robotgameredux.CommandsInterfaces;

import robotgameredux.actors.Faction;
import robotgameredux.core.Coordinates;
import robotgameredux.weapons.Pistol;
import robotgameredux.weapons.Weapon;

public interface AttackCommandInterface<T> extends Command{
	
	Weapon getActiveWeapon();
	Faction getFaction();
	Coordinates getTarget();
	Coordinates getCoords();
	
}
