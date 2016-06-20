package robotgameredux.input;

import Exceptions.InvalidTargetException;
import robotgameredux.core.Vector2;
import robotgameredux.weapons.Weapon;

public class AttackCommand implements Command {

	public AttackCommand(Integer activeWeaponIndex, Vector2 target, Attacker robot) {
		this.activeWeaponIndex = activeWeaponIndex;
		this.target = target;
		this.robot = robot;
	}
	
	@Override
	public void execute() throws InvalidTargetException {
		robot.setCommand(null);
		robot.getBattleSystem().execute(this);
	}

	public int getEnergy(){
		return robot.getEnergy();
	}
	
	public void setEnergy(int n) {
		robot.setEnergy(n);
	}	
	
	public Integer getActiveWeaponIndex() {
		return activeWeaponIndex;
	}
	public Vector2 getTarget() {
		return target;
	}
	public Attacker getRobot() {
		return robot;
	}

	public Weapon getActiveWeapon(int index) {
		return robot.getActiveWeapon(index);
	}
	
	public Faction getFaction() {
		return robot.getFaction();
	}
	
	public void setState(RobotStates state) {
		robot.setState(state);
	}

	private Integer activeWeaponIndex;
	private Vector2 target;
	private Attacker robot;
	
}
 