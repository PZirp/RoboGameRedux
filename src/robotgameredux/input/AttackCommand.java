package robotgameredux.input;

import robotgameredux.core.Vector2;

public class AttackCommand implements Command {

	public AttackCommand(Integer activeWeaponIndex, Vector2 target, Attacker robot) {
		this.activeWeaponIndex = activeWeaponIndex;
		this.target = target;
		this.robot = robot;
	}
	
	@Override
	public void execute() {
		robot.setCommand(null);
		robot.getBattleSystem().execute(this);
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


	private Integer activeWeaponIndex;
	private Vector2 target;
	private Attacker robot;
	
}
