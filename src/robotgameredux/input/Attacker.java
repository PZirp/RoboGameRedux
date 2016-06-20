package robotgameredux.input;

import robotgameredux.core.Vector2;
import robotgameredux.systems.BattleSystem;
import robotgameredux.systems.StandardBattleSystem;
import robotgameredux.weapons.Weapon;

public interface Attacker {

	public Weapon getActiveWeapon(Integer i);
	//public Vector2 getTarget();
	public BattleSystem getBattleSystem();
	public void setCommand(Command c);
	public void setState(RobotStates state);
	public Faction getFaction(); 
	public int getEnergy();
	public void setEnergy(int n);
}
