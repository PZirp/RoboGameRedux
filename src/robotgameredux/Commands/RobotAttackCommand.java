package robotgameredux.Commands;

import java.io.Serializable;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.CommandsInterfaces.AttackCommandInterface;
import robotgameredux.actors.AttackRobot;
import robotgameredux.actors.Faction;
import robotgameredux.core.Coordinates;
import robotgameredux.input.RobotStates;
import robotgameredux.weapons.Pistol;
import robotgameredux.weapons.Weapon;

public class RobotAttackCommand implements AttackCommandInterface<AttackRobot>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7414566496896963385L;
	public RobotAttackCommand(Integer activeWeaponIndex, Coordinates target, AttackRobot robot) {
		this.activeWeaponIndex = activeWeaponIndex;
		this.target = target;
		this.robot = robot;
	}
	
	@Override
	public Boolean execute() throws InvalidTargetException, InsufficientEnergyException {
		robot.setCommand(null);
		return robot.getBattleSystem().execute(this);
		
	}

	public int getEnergy(){
		return robot.getEnergy();
	}

	public Coordinates getTarget() {
		return target;
	}
	public AttackRobot getRobot() {
		return robot;
	}

	public Weapon getActiveWeapon() {
		return robot.getActiveWeapon(activeWeaponIndex);
	}
	
	public Faction getFaction() {
		return robot.getFaction();
	}
	
	public void setState(RobotStates state) {
		robot.setState(state);
	}

	@Override
	public void removeEnergy(int n) {
		robot.removeEnergy(n);
		
	}	
	
	@Override
	public Coordinates getCoords() {
		return robot.getCoords();
	}
	
	private Integer activeWeaponIndex;
	private Coordinates target;
	private AttackRobot robot;


	
}
 