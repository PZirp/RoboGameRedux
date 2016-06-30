package robotgameredux.systemsImplementations;

import java.io.Serializable;

import Exceptions.InvalidTargetException;
import robotgameredux.Commands.AttackCommand;
import robotgameredux.actors.AttackRobot;
import robotgameredux.actors.Attacker;
import robotgameredux.actors.Robot;
import robotgameredux.core.GameWorld;
import robotgameredux.core.RobotFactory;
import robotgameredux.core.Vector2;
import robotgameredux.input.RobotStates;
import robotgameredux.systemInterfaces.BattleSystem;
import robotgameredux.weapons.Bullet;
import robotgameredux.weapons.Weapon;;

public class StandardBattleSystem implements BattleSystem, Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4306228035835923018L;
	public StandardBattleSystem(RobotFactory rf, GameWorld gw) {
		this.actorsManager = rf;
		this.gameWorld = gw;
	}
	
	public Boolean execute(AttackCommand command) throws InvalidTargetException{
		
		Integer index = command.getActiveWeaponIndex();
		Vector2 target = command.getTarget();
		Weapon weapon = command.getActiveWeapon(index);
		Robot targeted = actorsManager.isRobot(target);
		if (targeted == null && weapon.hasBullets()) {
			throw new InvalidTargetException(command);
		}
				
		// Ci sta, perchè così il robot non diventa inattivo, GameManager cattura l'eccezione, e fa rifare il ciclo della scelta dell'azione e dell'arma
		if (targeted.getFaction() != command.getFaction()) {
			Bullet bullet = weapon.fire();
			command.setState(RobotStates.IDLE);
			//bullet.hit(targeted);
			//Rimettere bullet hit come prima e fare la differenza tra arma difensiva ed offensiva, aggiungere costo dell'attacco (in base all'arma)
			//Rendere Weapon solo un'interfaccia, creare altre armi
			targeted.damage(bullet);
			return true;
		} else {
			throw new InvalidTargetException(command);
		}
		
		//return false;
	}
	
	private GameWorld gameWorld;
	private RobotFactory actorsManager;

}
