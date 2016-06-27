package robotgameredux.Commands;

import java.io.Serializable;
import java.util.ArrayList;

import Exceptions.InvalidTargetException;
import robotgameredux.actors.AttackRobot;
import robotgameredux.actors.SupportRobot;
import robotgameredux.core.Vector2;
import robotgameredux.input.RobotStates;
import robotgameredux.tools.UsableTool;
import robotgameredux.weapons.Weapon;

public class SupInteractCommand implements Command, Serializable {

	public SupInteractCommand(SupportRobot robot, Vector2 target) {
		this.robot = robot;
		this.target = target;
	}

	@Override
	public Boolean execute() throws InvalidTargetException {
		System.out.println("All'interno del comando 0000000000000000000000000000");

		robot.setCommand(null);
		return robot.getInteractionSystem().execute(this);
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
	
	public void removeEnergy(int n) {
		robot.removeEnergy(n);
	}
	
	public ArrayList<UsableTool> getInventory() {
		return robot.getTools();
	}
	
	public void addTool(UsableTool t) {
		robot.addTool(t);
	}
	
	private SupportRobot robot;
	private Vector2 target;
	
}
