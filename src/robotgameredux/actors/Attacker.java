package robotgameredux.actors;

import robotgameredux.Commands.Command;
import robotgameredux.input.RobotStates;
import robotgameredux.systemInterfaces.BattleSystem;
import robotgameredux.weapons.Weapon;

public interface Attacker {

	public Weapon getActiveWeapon(Integer i);
	//public Vector2 getTarget();
	public BattleSystem getBattleSystem();
	public void setCommand(Command c);
	public void setState(RobotStates state);
	public Faction getFaction(); 
	public int getEnergy();
	public void removeEnergy(int n);
}
