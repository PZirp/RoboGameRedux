package robotgameredux.systems;

import Exceptions.InvalidTargetException;
import robotgameredux.actors.AttackRobot;
import robotgameredux.actors.Robot;
import robotgameredux.core.GameWorld;
import robotgameredux.core.RobotFactory;
import robotgameredux.core.Vector2;
import robotgameredux.input.AttackCommand;
import robotgameredux.input.Attacker;
import robotgameredux.input.RobotStates;
import robotgameredux.weapons.Bullet;
import robotgameredux.weapons.Weapon;
import robotgameredux.systems.BattleSystem;;

public class StandardBattleSystem implements BattleSystem{

	public StandardBattleSystem() {
		//this.actorsManager = actorsManager; 
	}
	
	public void setActorManager(RobotFactory rf) {
		this.actorsManager = rf;
	}
	
	public void setGameWorld(GameWorld gw) {
		this.gameWorld = gw;
	}
	//Da decidere se farsi passare direttamente l'arma attiva oppure tutto il robot
	
	
	public void execute(AttackCommand command) throws InvalidTargetException{
		
		Integer index = command.getActiveWeaponIndex();
		Vector2 target = command.getTarget();
		//Attacker actor = command.getRobot();
		//Rifare il comando facendo in modo che incapsuli il robot
		
	//public Boolean attemptAttack(Attacker robot, Vector2 target) {
		Robot targeted = actorsManager.isRobot(target);
		if (targeted == null) {
			throw new InvalidTargetException(command);
		}
		//System.out.println("TARGETTTTTTTTTTTTTTTTTTTTTT" + target);
		//System.out.println("TARGETED ROBOTTTTTTTTTTTTTTTTTTTTT" + targeted);
		
		
		/*if (index == -1) {
			actor.setState(RobotStates.INACTIVE);
			return;
		}*/
		
		Weapon weapon = command.getActiveWeapon(index);
		
		if (weapon.hasBullets() && targeted != null) {
			//if (!actorsManager.isRobot(target))
			//	InvalidTargetException() ?
			// Ci sta, perchè così il robot non diventa inattivo, GameManager cattura l'eccezione, e fa rifare il ciclo della scelta dell'azione e dell'arma
			if (targeted.getFaction() != command.getFaction()) {
				Bullet bullet = weapon.fire();
				bullet.hit(targeted);
			} else {
				throw new InvalidTargetException(command);
			}
				
			//return true;
		}
		command.setState(RobotStates.INACTIVE);
		//return false;
	}
	
	private GameWorld gameWorld;
	private RobotFactory actorsManager;

}
