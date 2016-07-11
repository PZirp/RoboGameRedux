package robotgameredux.CommandsInterfaces;

import java.util.ArrayList;

import robotgameredux.core.Coordinates;
import robotgameredux.input.RobotStates;
import robotgameredux.weapons.Pistol;
import robotgameredux.weapons.Weapon;

public interface AtkInteractCommandInterface<T> extends Command {

	public RobotStates getState();
	public Coordinates getCoords();
	public int getStrenght();
	public void addWeapon(Weapon w);
	public Coordinates getTarget();
	void addEnergy(Integer charge);
	
}
