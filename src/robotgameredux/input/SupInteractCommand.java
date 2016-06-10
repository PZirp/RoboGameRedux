package robotgameredux.input;

import java.util.ArrayList;

import robotgameredux.actors.AttackRobot;
import robotgameredux.actors.SupportRobot;
import robotgameredux.core.Vector2;
import robotgameredux.tools.UsableTool;
import robotgameredux.weapons.Weapon;

public class SupInteractCommand implements Command {

	public SupInteractCommand(SupportRobot robot, Vector2 target) {
		this.robot = robot;
		this.target = target;
	}

	@Override
	public void execute() {
		System.out.println("All'interno del comando 0000000000000000000000000000");

		robot.setCommand(null);
		robot.getInteractionSystem().execute(this);
	}
	
	public Vector2 getTarget() {
		return target;
	}
	
	public RobotStates getState() {
		return robot.getState();
	}
	
	public void setState(RobotStates state) {
		robot.setState(state);
	}
	
	public Vector2 getCoords() {
		return robot.getCoords();
	}

	public int getStrenght() {
		return robot.getStrenght();
	}
	
	public void addEnergy(Integer charge) {
		robot.addEnergy(charge);
	}
	
/*	public Robot getRobot() {
		return robot;
	}*/
	
	public ArrayList<UsableTool> getInventory() {
		return robot.getTools();
	}
	
	public void addWeapon(UsableTool t) {
		robot.addTool(t);
	}
	
	private SupportRobot robot;
	private Vector2 target;
	
}
