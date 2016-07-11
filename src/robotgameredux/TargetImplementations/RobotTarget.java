package robotgameredux.TargetImplementations;

import robotgameredux.TargetInterfaces.TargetInterface;
import robotgameredux.actors.Faction;
import robotgameredux.actors.Robot;

public class RobotTarget implements TargetInterface<Robot>{

	/*
	 *  Wrapper target per attore generico.
	 */
	
	public RobotTarget(Robot robot) {
		this.robot = robot;
	}
	
	@Override
	public int getHealth() {
		return robot.getHealth();
	}

	@Override
	public int getDefense() {
		return robot.getDefense();
	}

	@Override
	public void setDefense(int defense) {
		robot.setDefense(defense);
	}	
	
	@Override
	public void applyDamage(int damage) {
		robot.damage(damage);
	}

	@Override
	public Faction getFaction() {
		return robot.getFaction();
	}

	@Override
	public int getEnergy() {
		return robot.getEnergy();
	}

	@Override
	public void addEnergy(int energy) {
		robot.addEnergy(energy);
	}
	
	@Override
	public void heal(int health) {
		robot.heal(health);
	}
	
	Robot robot;

	
}
