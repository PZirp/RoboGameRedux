package robotgameredux.systems;

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
	
	
	public void execute(AttackCommand command) {
		
		Integer index = command.getActiveWeaponIndex();
		Vector2 target = command.getTarget();
		Attacker actor = command.getRobot();
		
		
	//public Boolean attemptAttack(Attacker robot, Vector2 target) {
		Robot targeted = actorsManager.isRobot(target);
		
		
		
		//System.out.println("TARGETTTTTTTTTTTTTTTTTTTTTT" + target);
		//System.out.println("TARGETED ROBOTTTTTTTTTTTTTTTTTTTTT" + targeted);
		Weapon weapon = actor.getActiveWeapon(index);
		
		if (weapon.hasBullets() && targeted != null) {
			Bullet bullet = weapon.fire();
			bullet.hit(targeted);
			//return true;
		}
		actor.setState(RobotStates.INACTIVE);
		//return false;
	}
	
	private GameWorld gameWorld;
	private RobotFactory actorsManager;

}
