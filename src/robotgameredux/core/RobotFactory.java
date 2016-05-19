package robotgameredux.core;

import java.util.ArrayList;

import robotgameredux.actors.AttackRobot;
import robotgameredux.actors.AttackRobotController;
import robotgameredux.actors.Robot;
import robotgameredux.actors.RobotType;
import robotgameredux.actors.SupportRobot;
import robotgameredux.actors.SupportRobotController;
import robotgameredux.input.Faction;
import robotgameredux.tools.HealthPack;
import robotgameredux.weapons.Weapon;

public class RobotFactory {
	
	private MovementSystem ms;
	
	public RobotFactory(GameWorld reference, AttackRobotController attackRobotController, SupportRobotController supportRobotController, BattleSystem battleSystem, MovementSystem ms) {
		this.reference = reference;
		this.attackRobotController = attackRobotController;
		this.supportRobotController = supportRobotController;
		this.attackRobots= new ArrayList<AttackRobot>();
		this.supportRobots= new ArrayList<SupportRobot>();
		this.battleSystem = battleSystem;
		this.ms = ms;
	}
	
	public Robot createRobot(Faction faction, Vector2 position, RobotType type) {
		
		if (type == RobotType.ATTACK) {
			return createAttack(faction, position);
		} else if (type == RobotType.SUPPORT) {
			return createSupport(faction, position);
		}
		
		return null;
	}
	
	private AttackRobot createAttack(Faction faction, Vector2 position) {
		AttackRobot newRobot = new AttackRobot(reference, position, battleSystem, ms);
		newRobot.setFaction(faction);
		newRobot.addWeapon(new Weapon(newRobot));
		this.attackRobotController.addRobot(newRobot);
		this.attackRobots.add(newRobot);
		return newRobot;
	}
	
	private SupportRobot createSupport(Faction faction, Vector2 position) {
		SupportRobot newRobot = new SupportRobot(reference, position, ms);
		newRobot.setFaction(faction);
		newRobot.addTool(new HealthPack());
		this.supportRobotController.addRobot(newRobot);
		this.supportRobots.add(newRobot);
		return newRobot;
	}
	
	/*
	 * Controlla se esiste un robot che si trova nella posizione indicata, se esiste lo ritorna
	 * @param Posizione in cui si deve trovare il robot
	 * @return null se non c'è nessun robot alla posizione indicata, il robot trovato altrimenti
	 */
	
	public Robot isRobot(Vector2 target) {
		for (Robot r : attackRobots) {
			if (r.getCoords().dst(target) == 0) 
				return r;
		}
		for (Robot r : supportRobots) {
			if (r.getCoords().dst(target) == 0) 
				return r;
		}
		
		return null;
	}
	
	public void remove(Robot robot) {
		attackRobots.remove(robot);
		supportRobots.remove(robot);
	}
	
	private GameWorld reference;
	private AttackRobotController attackRobotController;
	private SupportRobotController supportRobotController;
	private ArrayList<AttackRobot> attackRobots;
	private ArrayList<SupportRobot> supportRobots;
	private BattleSystem battleSystem;
}
