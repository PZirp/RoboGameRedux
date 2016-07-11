package robotgameredux.systemsImplementations;

import java.io.Serializable;

import javax.swing.JOptionPane;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.CommandsInterfaces.AttackCommandInterface;
import robotgameredux.TargetInterfaces.TargetInterface;
import robotgameredux.core.IActorManager;
import robotgameredux.core.RobotFactory;
import robotgameredux.core.Coordinates;
import robotgameredux.systemInterfaces.BattleSystem;
import robotgameredux.weapons.IBullet;
import robotgameredux.weapons.Weapon;
import robotgameredux.weapons.WeaponType;;

public class StandardBattleSystem implements BattleSystem, Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4306228035835923018L;
	
	public StandardBattleSystem(RobotFactory rf) {
		this.actorsManager = rf;
	}
	
	public <T> Boolean execute(AttackCommandInterface<T> command) throws InvalidTargetException, InsufficientEnergyException {
		
		Coordinates target = command.getTarget();
		Weapon weapon = command.getActiveWeapon();
		TargetInterface<?> targeted = null;
		
		if (command.getEnergy() < weapon.getCost()) {
			throw new InsufficientEnergyException(command);
		}		
		if(actorsManager.isRobot(target) == true) {
			targeted = actorsManager.getTarget(target);
		} else {
			throw new InvalidTargetException(command);			
		}				
		Boolean result = checkTrajectory(command.getCoords(), command.getTarget());
		if (weapon.getType() == WeaponType.OFFENSIVE && targeted.getFaction() == command.getFaction()) {
			throw new InvalidTargetException(command);
		}	
		if (weapon.getType() == WeaponType.DEFENSIVE && targeted.getFaction() != command.getFaction()) {
			throw new InvalidTargetException(command);
		}		
		if (result == true) {
			IBullet bullet = weapon.fire();
			command.removeEnergy(weapon.getCost());
			bullet.hit(targeted);
			return true;
		}	else {
			throw new InvalidTargetException(command);
		}
		
		/*
		if (result == true) {
			if (weapon.getType() == WeaponType.OFFENSIVE) {	
				if (targeted.getFaction() != command.getFaction()) {
					Bullet bullet = weapon.fire();
					command.removeEnergy(weapon.getCost());
					bullet.hit(targeted);
					command.setState(RobotStates.IDLE);
					return true;
				} else {
					throw new InvalidTargetException(command);
				}
			} else if (weapon.getType() == WeaponType.DEFENSIVE) {
				if (targeted.getFaction() == command.getFaction()) {
					Bullet bullet = weapon.fire();
					command.removeEnergy(weapon.getCost());					
					bullet.hit(targeted);					
					command.setState(RobotStates.IDLE);
					return true;
				} else {
					throw new InvalidTargetException(command);
				}
			}
		} else {
			throw new InvalidTargetException(command);
		}*/
		//return false;
	}
	
	private Boolean checkTrajectory(Coordinates origin, Coordinates target) {
		Coordinates direction = origin.sub(target)/*Coordinates.sub(origin, target)*/;
		if (direction.getX() > 0 && direction.getY() == 0) {
			return true;
		}
		if (direction.getX() < 0 && direction.getY() == 0) {
			return true;
		}
		if (direction.getX() == 0 && direction.getY() > 0) {
			return true;
		}
		if (direction.getX() == 0 && direction.getY() < 0) {
			return true;
		}
		if (direction.getX() == 0 && direction.getY() == 0) {
			return true;
		}
		return false;
	}

	private IActorManager actorsManager;

}
