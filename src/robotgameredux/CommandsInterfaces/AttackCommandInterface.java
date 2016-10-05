package robotgameredux.CommandsInterfaces;

import robotgameredux.core.Coordinates;
import robotgameredux.enums.Faction;
import robotgameredux.weapons.Weapon;

public interface AttackCommandInterface<T> extends Command {

	Weapon getActiveWeapon();

	Faction getFaction();

	Coordinates getTarget();

	Coordinates getCoords();

}
