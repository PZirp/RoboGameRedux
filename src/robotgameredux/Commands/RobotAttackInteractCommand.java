package robotgameredux.Commands;

import java.io.Serializable;
import java.util.ArrayList;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.CommandsInterfaces.AtkInteractCommandInterface;
import robotgameredux.actors.AttackRobot;
import robotgameredux.core.Coordinates;
import robotgameredux.input.RobotStates;
import robotgameredux.weapons.Pistol;
import robotgameredux.weapons.Weapon;

public class RobotAttackInteractCommand implements AtkInteractCommandInterface<AttackRobot>, Serializable {

	/*
	 * Implementazione dell'interfaccia AtckInteractCommandInterface specifica per i tipi AttackRobot
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8878613123004990541L;
	public RobotAttackInteractCommand(AttackRobot robot, Coordinates target) {
		this.robot = robot;
		this.target = target;
	}

	@Override
	public Boolean execute() throws InvalidTargetException, InsufficientEnergyException {
		robot.setCommand(null);
		return robot.getInteractionSystem().execute(this);
	}
	@Override
	public Coordinates getTarget() {
		return target;
	}
	@Override
	public RobotStates getState() {
		return robot.getState();
	}
	@Override
	public void setState(RobotStates state) {
		robot.setState(state);
	}
	@Override
	public Coordinates getCoords() {
		return robot.getCoords();
	}

	@Override
	public int getStrenght() {
		return robot.getStrenght();
	}
	@Override
	public void addEnergy(Integer charge) {
		robot.addEnergy(charge);
	}
	@Override
	public void removeEnergy(int n) {
		robot.removeEnergy(n);
	}
	
	@Override
	public void addWeapon(Weapon w) {
		robot.addWeapon(w);
	}
	
	@Override
	public int getEnergy() {
		return robot.getEnergy();
	}
	
	private AttackRobot robot;
	private Coordinates target;
	
	
}
