package robotgameredux.core;

import robotgameredux.actors.AttackRobot;
import robotgameredux.actors.Robot;
import robotgameredux.weapons.Bullet;
import robotgameredux.weapons.Weapon;

public class BattleSystem {

	public BattleSystem() {
		//this.actorsManager = actorsManager; 
	}
	
	public void setActorManager(RobotFactory rf) {
		this.actorsManager = rf;
	}
	
	public void setGameWorld(GameWorld gw) {
		this.gameWorld = gw;
	}
	//Da decidere se farsi passare direttamente l'arma attiva oppure tutto il robot
	public Boolean attemptAttack(AttackRobot robot, Vector2 target) {
		Robot targeted = actorsManager.isRobot(target);
		System.out.println("TARGETTTTTTTTTTTTTTTTTTTTTT" + target);
		System.out.println("TARGETED ROBOTTTTTTTTTTTTTTTTTTTTT" + targeted);
		Weapon weapon = robot.getActiveWeapon();
		
		if (weapon.hasBullets() && targeted != null) {
			Bullet bullet = weapon.fire();
			bullet.hit(targeted);
			return true;
		}
		return false;
	}
	
	private GameWorld gameWorld;
	private RobotFactory actorsManager; 
}
