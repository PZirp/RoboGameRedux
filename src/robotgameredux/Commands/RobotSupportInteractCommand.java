package robotgameredux.Commands;

import java.io.Serializable;
import java.util.ArrayList;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.CommandsInterfaces.SupInteractCommandInterface;
import robotgameredux.actors.AttackRobot;
import robotgameredux.actors.SupportRobot;
import robotgameredux.core.Coordinates;
import robotgameredux.input.RobotStates;
import robotgameredux.tools.UsableTool;
import robotgameredux.weapons.Pistol;

public class RobotSupportInteractCommand implements SupInteractCommandInterface<SupportRobot>, Serializable {

	/*
	 * Implementazione dell'interfaccia SupInteractCommandInterface<T> per il tipo SupportRobot
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6872378134853586932L;
	public RobotSupportInteractCommand(SupportRobot robot, Coordinates target) {
		this.robot = robot;
		this.target = target;
	}

	@Override
	public Boolean execute() throws InvalidTargetException, InsufficientEnergyException {
		robot.setCommand(null);
		return robot.getInteractionSystem().execute(this);
	}
	
	public Coordinates getTarget() {
		return target;
	}
	
	public RobotStates getState() {
		return robot.getState();
	}
	
	public void setState(RobotStates state) {
		robot.setState(state);
	}
	
	public Coordinates getCoords() {
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
	
/*public ArrayList<UsableTool> getInventory() {
		return robot.getTools();
	}*/
	
	public void addTool(UsableTool t) {
		robot.addTool(t);
	}
	
	public int getEnergy() {
		return robot.getEnergy();
	}
	
	private SupportRobot robot;
	private Coordinates target;


	
}
